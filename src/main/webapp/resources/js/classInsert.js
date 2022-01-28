// 브이월드 행정도 API
$.support.cors = true;
            
            $(function(){
                $.ajax({
                    type: "get",
                    url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIDO_INFO",
                    async: false,
                    dataType: 'jsonp',
                    success: function(data) {
                        let html = "<option>선택</option>";
        
                        data.response.result.featureCollection.features.forEach(function(f){
                            //console.log(f.properties.ctp_kor_nm);
                            let 행정구역코드 = f.properties.ctprvn_cd;
                            let 행정구역명 = f.properties.ctp_kor_nm;
                            
                            if(행정구역명 == "충청북도"){
                                행정구역명 = "충북";
                            }else if(행정구역명 == "충청남도"){
                                행정구역명 = "충남";
                            }else if(행정구역명 == "경상남도"){
                                행정구역명 = "경남";
                            }else if(행정구역명 == "경상북도"){
                                행정구역명 = "경북";
                            }else if(행정구역명 == "전라남도"){
                                행정구역명 = "전남";
                            }else if(행정구역명 == "전라북도"){
                                행정구역명 = "전북";
                            }else{
                                행정구역명 = 행정구역명.substring(0,2);
                            }
                            


                            html +=`<option value="${행정구역코드}">${행정구역명}</option>`
                            
                        })
                        
                        $('#sido_code').html(html);
                        
                    },
                    error: function(xhr, stat, err) {}
                });
                
                
                $(document).on("change","#sido_code",function(){
                    let thisVal = $(this).val();		
        
                    $.ajax({
                        type: "get",
                        url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIGG_INFO",
                        data : {attrfilter : 'sig_cd:like:'+thisVal},
                        async: false,
                        dataType: 'jsonp',
                        success: function(data) {
                            let html = "<option>선택</option>";
        
                            data.response.result.featureCollection.features.forEach(function(f){
                                let 행정구역코드 = f.properties.sig_cd;
                                let 행정구역명 = f.properties.sig_kor_nm;
                                
                                html +=`<option value="${행정구역코드}">${행정구역명}</option>`
                                
                            })
                            $('#sigoon_code').html(html);
                            
                        },
                        error: function(xhr, stat, err) {}
                    });
                });
                
                $(document).on("change","#sigoon_code",function(){ 
                    
                    let thisVal = $(this).val();		
        
                    $.ajax({
                        type: "get",
                        url: "https://api.vworld.kr/req/data?key=CEB52025-E065-364C-9DBA-44880E3B02B8&domain=http://localhost:8080&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADEMD_INFO",
                        data : {attrfilter : 'emd_cd:like:'+thisVal},
                        async: false,
                        dataType: 'jsonp',
                        success: function(data) {
                            let html = "<option>선택</option>";
        
                            data.response.result.featureCollection.features.forEach(function(f){
                                console.log(f.properties)
                                let 행정구역코드 = f.properties.emd_cd;
                                let 행정구역명 = f.properties.emd_kor_nm;
                                html +=`<option value="${행정구역코드}">${행정구역명}</option>`
                                
                            })
                            $('#dong_code').html(html);
                            
                        },
                        error: function(xhr, stat, err) {}
                    });
        
                });
        
            });


//---------------------------------------------------------------
// 수업참여인원
$("#solo-class").on("click", function(){
    //$("#group-input").html("");
    $("#group-input").slideUp(250);
})

$("#group-class").on("click", function(){
    //$("#group-input").html('<input type="number" id="peopel-count1" class="input-style"> &nbsp명~&nbsp <input type="number" id="peopel-count2" class="input-style" required>');
    $("#group-input").slideDown(250);
})


// 카테고리 옵션
$("#ct1").on("change", function(){
    let c = $("#ct1 option:checked").text()

    $("select#ct1 option[value='base']").remove();
    $("select#ct2 option").remove();

    if(c == "공예/디자인"){
        $("select#ct2").append("<option>조향,캔들,비누</option>");
        $("select#ct2").append("<option>가죽,목공,도예</option>");
        $("select#ct2").append("<option>플라워</option>");
        $("select#ct2").append("<option>캘리그라피</option>");
        $("select#ct2").append("<option>디지털드로잉</option>");
        $("select#ct2").append("<option>취미미술</option>");
    }else if(c == "요리"){
        $("select#ct2").append("<option>베이킹,디저트</option>");
        $("select#ct2").append("<option>커피,차,음료</option>");
        $("select#ct2").append("<option>건강,다이어트식</option>");
        $("select#ct2").append("<option>세계요리</option>");
    }else if(c == "뷰티/헬스"){
        $("select#ct2").append("<option>메이크업</option>");
        $("select#ct2").append("<option>퍼스널컬러</option>");
        $("select#ct2").append("<option>패션</option>");
        $("select#ct2").append("<option>PT,GX</option>");
    }else if(c == "사진/영상"){
        $("select#ct2").append("<option>사진</option>");
        $("select#ct2").append("<option>영상</option>");
    }else if(c == "커리어"){
        $("select#ct2").append("<option>개발</option>");        
        $("select#ct2").append("<option>언어,외국어</option>");        
        $("select#ct2").append("<option>주식투자</option>");        
        $("select#ct2").append("<option>자격증</option>");        
    }else if(c == "음악"){
        $("select#ct2").append("<option>보컬</option>");      
        $("select#ct2").append("<option>악기</option>");      
        $("select#ct2").append("<option>작곡,디제잉</option>");      
        $("select#ct2").append("<option>댄스</option>");      
    }else{
        $("select#ct2").append("<option>기타</option>");      
    }

})


// 썸머노트
$(document).ready(function() {
    $('#summernote').summernote({ // summernote를 사용하기 위한 선언
        height: 400,
        width: 750,      
    });
});


// 제목 글자수
$("#titleArea").on("input", function(){
    // 현재 작성된 글자 수를 변수에 저장
    let count = $(this).val().length;
    // 넘으면 빨간색
    if( count >= 50 ){
        $("#titleText-count").css("color", "red");
        $(this).val( $(this).val().substr(0,50) );
        count = 50;
    }
    // 아니면 검정색
    else{
        $("#titleText-count").css("color", "black");
    }

    // 글자 수를 출력하는 span
    $("#titleText-count").text( count ); 
});

