package edu.kh.tteutto.member.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.tteutto.chat.model.vo.ChatRoom;
import edu.kh.tteutto.classRoom.model.vo.ClassRefund;
import edu.kh.tteutto.classRoom.model.vo.ClassRegister;
import edu.kh.tteutto.classRoom.model.vo.ClassReport;
import edu.kh.tteutto.classRoom.model.vo.ClassReview;
import edu.kh.tteutto.classRoom.model.vo.Teacher;
import edu.kh.tteutto.common.Util;
import edu.kh.tteutto.main.model.vo.ClassList;
import edu.kh.tteutto.main.model.vo.Pagination;
import edu.kh.tteutto.member.model.dao.MemberDAO;
import edu.kh.tteutto.member.model.vo.Career;
import edu.kh.tteutto.member.model.vo.Certified;
import edu.kh.tteutto.member.model.vo.Member;
import edu.kh.tteutto.member.model.vo.Sns;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDAO dao;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	

	// 로그인
	@Override
	public Member login(Member member) {
		
		String encPw = encoder.encode(member.getMemberPw());
		Member loginMember = dao.login(member.getMemberEmail());

		if(loginMember != null && encoder.matches(member.getMemberPw(), loginMember.getMemberPw())) {
			
			loginMember.setMemberPw(null);
		}else {
			loginMember = null;
		}
		return loginMember;
	}
	
	// 이메일 중복 검사
	@Override
	public int emailDupCheck(String inputEmail) {
		return dao.emailDupCheck(inputEmail);
	}
	
	// 회원 인증테이블에 이메일 중복 확인
	@Override
	public int emailDupCheck2(String inputEmail) {
		return dao.emailDupCheck2(inputEmail);
	}
	
	// 회원가입 이메일 인증번호 저장
	@Override
	public int sendMailTest(Map<String, String> map) {
		return dao.sendMailTest(map);
	}
		
	
	// 회원가입 이메일 인증번호 수정
	@Override
	public int updateMailTest(Map<String, String> map) {
		return dao.updateMailTest(map);
	}

	// 이메일 인증 확인
	@Override
	public int checkCert(Map<String, String> map) {
		return dao.checkCert(map);
	}

	// 회원가입
	@Transactional("rollbackFor = Exception.class")
	@Override
	public int signUp(Member member) {
		
		String encPw = encoder.encode(member.getMemberPw());
		
		member.setMemberPw(encPw);
		
		return dao.signUp(member);
	}

	// 비밀번호 변경전 확인
	@Override
	public int changeConfirm(Certified certified) {
		return dao.changeConfirm(certified);
	}

	// 비밀번호 변경
	@Override
	public int changePw(Member member) {
		
		String encPw = encoder.encode(member.getMemberPw());
		
		member.setMemberPw(encPw);
		return dao.changePw(member);
	}
	
	@Override
	public int updateCert(Certified certified) {
		return dao.updateCert(certified);
	}

	// 강사 정보 조회
	@Override
	public Teacher selectTeacherProfile(int memberNo) {
		return dao.selectTeacherProfile(memberNo);
	}
	
	// 강사 이력 조회
	@Override
	public List<Career> selectTeacherCareer(int memberNo) {
		return dao.selectTeacherCareer(memberNo);
	}
	
	// 강사 sns 조회
	@Override
	public List<Sns> selectTeacherSns(int memberNo) {
		return dao.selectTeacherSns(memberNo);
	}
	
	// 강사 정보 수정
	@Override
	public int teacherProfileUpdate(Teacher teacher, String phone, List<Sns> snsList, List<String> profileInput,
		List<MultipartFile> images, String webPath, String serverPath) {
		
		int result = 0; 
		int result4 = 0;
		
		// 전화번호, 강사 소개에 XSS 처리 + 강사 소개에 개행문자 변경처리
		phone = (Util.XSS(phone));
		teacher.setTeacherIntro(Util.XSS(teacher.getTeacherIntro()));
		teacher.setTeacherIntro(Util.changeNewLine(teacher.getTeacherIntro()));	// 개행문자 처리
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("memberNo", teacher.getMemberNo());
		map1.put("phone", phone);
		
		
		// 전화번호 업데이트
		int result1 = dao.teacherPhoneUpdate(map1);
		
		if(result1 > 0) {
			// 강사 소개 업데이트
			int result2 = dao.teacherIntroduceUpdate(teacher);
			
			if(result2 > 0 && snsList.size() != 0) {
				// sns 삭제
				dao.teacherSnsDelete(teacher.getMemberNo());
				
				// sns 삽입
				for(Sns sns : snsList) {
					sns.setMemberNo(teacher.getMemberNo());
					result4 = dao.teacherSnsInsert(sns);
				}
				
			} else {
				result4 = 1;
			}
		}

		if(result4 > 0) {
			// images에 담겨있는 파일 정보 중 업로드된 파일 정보를 imgList에 옮겨 담기
			List<Career> imgList = new ArrayList<Career>();
			
			for(int i=0 ; i<images.size(); i++) {
				
				Career img = new Career();
				
				img.setMemberNo(teacher.getMemberNo()); // 강사 번호
				img.setImgPath(webPath); // 웹 접근 경로
				img.setImgName( Util.fileRename( images.get(i).getOriginalFilename() ) ); // 변경된 파일명
				img.setImgOriginal( images.get(i).getOriginalFilename() ); // 원본 파일명
				img.setCareerContent(profileInput.get(i));	// 이력 내용
				
				imgList.add(img);
				
				result = dao.teacherProfileInsert(img);
				
				if(result > 0) {
					
					try {
						images.get(i).transferTo(new File(serverPath + "/" + imgList.get(i).getImgName()));
					} catch (Exception e) {
						e.printStackTrace();
						// 파일 변환이 실패할 경우
						// 사용자 정의 예외 발생
//						throw new InsertBoardFailException("파일 변환 중 문제 발생");
					}
				}
			}
		}
		
		return result;
	}
	
	// 강사 정보 수정 - 이력 수정 x
	@Override
	public int teacherProfileUpdate2(Teacher teacher, String phone, List<Sns> snsList) {
		
		int result = 0;
		
		// 전화번호, 강사 소개에 XSS 처리 + 강사 소개에 개행문자 변경처리
		phone = (Util.XSS(phone));
		teacher.setTeacherIntro(Util.XSS(teacher.getTeacherIntro()));
		teacher.setTeacherIntro(Util.changeNewLine(teacher.getTeacherIntro()));	// 개행문자 처리
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("memberNo", teacher.getMemberNo());
		map1.put("phone", phone);
		
		
		// 전화번호 업데이트
		int result1 = dao.teacherPhoneUpdate(map1);
		
		if(result1 > 0) {
			// 강사 소개 업데이트
			int result2 = dao.teacherIntroduceUpdate(teacher);
			
			if(result2 > 0) {
				
				if(snsList.size() != 0) {
					// sns 삭제
					dao.teacherSnsDelete(teacher.getMemberNo());
					
					// sns 삽입
					for(Sns sns : snsList) {
						sns.setMemberNo(teacher.getMemberNo());
						result = dao.teacherSnsInsert(sns);
					}
				} else {
					// sns 삭제
					dao.teacherSnsDelete(teacher.getMemberNo());
					result = 1;
				}
			}
		}
		return result;
	}

	// 강사 정보 수정 - 이력 삭제
	@Override
	public int teacherProfiledelete(String id, String webPath, String serverPath) {
		
		String selectImgName = dao.selectImgName(id);

		if(selectImgName.length() != 0) {
			String fileName = serverPath + selectImgName;

			//현재 게시판에 존재하는 파일객체를 만듬
			File file = new File(fileName);
			if(file.exists()) { // 파일이 존재하면
				file.delete(); // 파일 삭제	
			}
		}
		
		return dao.teacherProfiledelete(id);
	}

	// 학생 정보 수정
	@Override
	public int studentProfileUpdate(Member member) {
		return dao.studentProfileUpdate(member);
	}

	// 회원 탈퇴
	@Override
	public int memberResign(int memberNo) {
		
		if(dao.selectmemberResign(memberNo) == 0) {	// 수강중이거나 수강예정인 강사가 아닐 경우
			return dao.memberResign(memberNo);	// 수강중이거나 수강예정인 강사일 경우
		} else {
			return -1;
		}
	}

	// 강사 신청
	@Transactional
	@Override
	public int teacherRegisterInsert(Teacher teacher, List<MultipartFile> images, MultipartFile image, String careerContent, List<Sns> snsList, String serverPath, String serverPath2) {
		
		// 웹경로 프로필, 이력 이미지
		String webPath = "/resources/images/teacher/profile/"; 
		String webPath2 = "/resources/images/teacher/career/";
		
		// SNS 삽입
		if(!snsList.isEmpty()) {
			for(Sns sns : snsList) { 
				sns.setMemberNo(teacher.getMemberNo());
				dao.teacherSnsInsert(sns); 
				
			}
		}
		
		// 크로스사이트 ,개행문자
		teacher.setTeacherIntro(Util.XSS(teacher.getTeacherIntro()));
		teacher.setTeacherIntro(Util.changeNewLine(teacher.getTeacherIntro()));

		
		// 프로필사진
		if(image.getSize() != 0) {
			teacher.setTeacherImg(Util.fileRename(image.getOriginalFilename() )); // 변경된 파일명
			
			try {
				image.transferTo(new File(serverPath + "/" + teacher.getTeacherImg()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 강사 등록
		int result = dao.teacherRegisterInsert(teacher);
		

		// 캐리어 등록
		List<Career> career = new ArrayList<Career>();
		
		// name careerContent 값을 "," 기준 잘라서 배열로 넣고 다시 빼서 써야징
		if(careerContent != null) {
		if(careerContent.length() != 0) {
			
			// 캐리어 크로스, 개행
			careerContent = Util.changeNewLine(careerContent);
			careerContent = Util.XSS(careerContent);
			String[] carArray = careerContent.split(",");
			
			for(int c = 0; c<carArray.length; c++) {
				Career car = new Career();
				car.setCareerContent(carArray[c]);
				car.setMemberNo(teacher.getMemberNo());
				
				// 이미지 있으면 이미지도 넣으시오
				if(!images.get(c).getOriginalFilename().equals("")) {
					car.setImgPath(webPath2); // 웹접근경로
					car.setImgName(Util.fileRename(images.get(c).getOriginalFilename())); // 변경파일명
					car.setImgOriginal(images.get(c).getOriginalFilename()); // 원본파일명
					
				} 
				career.add(car);
			}
			
		}
		
		// 캐리어 이미지 성공했으면 파일 저장
		if (!career.isEmpty()) {
			int imginsert = dao.insertTeacherCareer(career);
			//System.out.println("캐리어는 몇 개 찍히니 " + career.size());
			//System.out.println("몇 개 받았니 " + imginsert);

			if (imginsert == career.size()) {
				for (int i = 0; i<career.size(); i++) {

					try {
						images.get(i).transferTo(new File(serverPath2 + "/" + career.get(i).getImgName()));
						//System.out.println(i + "번 추가");
					} catch (Exception e) {
						e.printStackTrace();

					}
	
				}
			}	
		}
		}
		
		return result;
	}
	
	// 클래스 개수 조회
	@Override
	public Pagination getPagination(Map<String, Object> map, int page) {
		int wishListCount = dao.getWishListCount(map);
		return new Pagination(wishListCount, page);
	}

	// 클래스 카드 조회
	@Override
	public List<ClassList> selectWishList(Pagination pagination, Map<String, Object> map) {
		return dao.selectWishList(pagination, map);
	}

	// 찜한 클래스 삽입 & 삭제
	@Override
	public int changeHeart(Map<String, Object> map) {
		int heart = 0;
		
		try {
			heart = dao.insertHeart(map);
			
		} catch (Exception e) {
			heart = dao.deleteHeart(map);
		}
		
		return heart;
	}

	// 학생 수강신청 목록 조회
	@Override
	public List<ClassRegister> studentClassList(int memberNo, Pagination pagination) {
		return dao.studentClassList(memberNo, pagination);
	}

	// 학생 클래스 신고
	@Override
	public int insertReport(ClassReport classReport) {
		return dao.insertReport(classReport);
	}

	// 학생 후기 작성
	@Override
	public int insertComment(ClassReview classReview) {
		return dao.insertComment(classReview);
	}

	// 학생 작성한 후기 목록 조회
	@Override
	public List<ClassReview> studentCommentList(int memberNo, Pagination pagination) {
		return dao.studentCommentList(memberNo, pagination);
	}

	// 학생 후기 수정
	@Override
	public int updateReport(ClassReview classReview) {
		return dao.updateReport(classReview);
	}

	// 학생 대 강사 채팅방 생성
	@Override
	public int insertChatRoom(ChatRoom chatRoom) {
		return dao.insertChatRoom(chatRoom);
	}

	// 채팅방이 이미 있는지 확인
	@Override
	public int checkChatRoom(ChatRoom chatRoom) {
		return dao.checkChatRoom(chatRoom); 
	}

	// 클래스 환불 신청
	@Override
	public int refundClass(ClassRefund refund) {
		return dao.refundClass(refund);
	}

	// 학생 후기 삭제
	@Override
	public int deleteReview(int reviewNo) {
		return dao.deleteReview(reviewNo);
	}

	// 후기 있는지 검사
	@Override
	public int searchReview(int regNo) {
		return dao.searchReview(regNo);
	}

	// 신고 있는지 검사
	@Override
	public int searchReport(int regNo) {
		return dao.searchReport(regNo);
	}

	// 환불 신청을 이미 했는지 검사
	@Override
	public int checkRefund(int regNo) {
		return dao.checkRefund(regNo);
	}

	// 내 수강신청 목록 카운트
	@Override
	public Pagination registerPagination(int memberNo, int cp) {
		int listCount = dao.registerCount(memberNo);
		
		return new Pagination(listCount, cp);
	}

	// 내 후기목록 카운트
	@Override
	public Pagination reviewPagination(int memberNo, int cp) {
		int listCount = dao.reviewCount(memberNo);
		
		return new Pagination(listCount, cp);
	}

	// 강사 신청 이미 했는지 체크
	@Override
	public int teacherSt(int memberNo) {
		return dao.teacherSt(memberNo);
	}
	
	// 강사 이미지 수정
	@Override
	public String teacherImgUpdate(String loginTeacherImg, int memberNo, MultipartFile teacherImage, String webPath, String serverPath) {
		
		boolean result = false;
		int result2 = 0;

		// 새로운 이미지 이름 변경
		String imgName = Util.fileRename( teacherImage.getOriginalFilename() );
		
		// 기존 이미지 삭제
		//현재 게시판에 존재하는 파일객체를 만듬
		String fileName = serverPath + loginTeacherImg;
		File file = new File(fileName);
		
		if(file.exists()) { // 파일이 존재하면
			result = file.delete(); // 파일 삭제
		}
		
		// 이미지 업데이트
		// 변경된 파일명
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("memberNo", Integer.toString(memberNo));
		map.put("imgName", imgName);
		result2 = dao.teacherImgUpdate(map);
		
		if(result2 > 0) {
			
			try {
				teacherImage.transferTo(new File(serverPath + "/" + imgName));
			} catch (Exception e) {
				e.printStackTrace();
				// 파일 변환이 실패할 경우
				// 사용자 정의 예외 발생
			}
			
			return imgName;
		} else {
			return null;
		}
		
	}
	
	// 비밀번호 변경
	@Override
	public int changePw2(Member member) {
		
		String encPw = encoder.encode(member.getMemberPw());
		
		member.setMemberPw(encPw);
		
		return dao.changePw2(member);
	}


	// 후기 작성전에 강의가 끝났는지 체크
	@Override
	public int overDateReview(int epNo) {
		return dao.overDateReview(epNo);
	}

}
