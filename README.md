## 👩‍🏫 뜨또 : 뜨겁게 또 시작해보자!
오프라인 클래스에 최적화된 클래스 플랫폼 뜨또에서 전국의 모든 오프라인 클래스를 찾아보세요.

![뜨또 001](https://user-images.githubusercontent.com/110330040/183922817-8309ad64-9e0c-48c5-99fe-59735f4cc9c9.png)

<br>

## ❓ 기획 의도
기존의 클래스 플랫폼들은 대부분 온라인 중심으로, 오프라인 클래스는 찾아보기 힘들다는 단점이 있었습니다. 뜨또팀은 기존의 사이트들을 벤치마킹하여 오프라인 클래스에 최적화된 클래스 플랫폼을 기획하였습니다.

 * **벤치마킹 사이트** : 
[클래스101](http://class101.net/), [탈잉](https://taling.me/), [아이디어스 온라인 클래스](https://www.idus.com/oc)

<br>

## 👋 프로젝트 소개
> 📝 뜨또에서는 전국의 모든 오프라인 클래스들을 수강할 수 있습니다.  
📍 **현재 위치 좌표**를 통해 내 주변의 클래스들을 한눈에 모아볼 수 있습니다.  
🗃️ 가격대, 지역, 수업 형태, 정렬 등 **다양한 검색 옵션**으로 나에게 맞는 클래스를 쉽게 찾을 수 있습니다.  
📊 클래스별 수강생의 **연령대와 성별 통계** 정보를 제공합니다.

<br>

## ⌛ 제작 기간
2021년 12월 31일 ~ 2022년 02월 16일 (2개월)

<br>

## ⚒️ 개발 환경 및 사용 기술
**[사용 기술]**
<p>
  <img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Oracle-F80000?style=flat-square&logo=Oracle&logoColor=white">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white">
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white">
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=JavaScript&logoColor=black">
  <img src="https://img.shields.io/badge/JSON-000000?style=flat-square&logo=JSON&logoColor=white">
  <img src="https://img.shields.io/badge/jQuery-0769AD?style=flat-square&logo=jQuery&logoColor=white">
</p>

**[개발 환경]**
<p>
  <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=flat-square&logo=Visual Studio Code&logoColor=white">
  <img src="https://img.shields.io/badge/Eclipse IDE-2C2255?style=flat-square&logo=Eclipse IDE&logoColor=white">
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white">
  <img src="https://img.shields.io/badge/Apache Tomcat-F8DC75?style=flat-square&logo=Apache Tomcat&logoColor=black">
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat-square&logo=Bootstrap&logoColor=white">
</p>

**[협업 툴]**
<p>
  <img src="https://img.shields.io/badge/Trello-0052CC?style=flat-square&logo=Trello&logoColor=white">
  <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=Slack&logoColor=white">
  <img src="https://img.shields.io/badge/Sourcetree-0052CC?style=flat-square&logo=Sourcetree&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white">
</p>

<br>

## ⛓️ ERD
![ERD(논리,물리)_뜨또](https://user-images.githubusercontent.com/110330040/186585403-977d28b5-c549-4150-9bbf-f8a007159103.png)

<br>

## 📂 폴더 구조
```
📦 tteutto
├── 📁 src/main
│   ├── 📁 java/edu/kh/tteutto
│   │   ├── 📁 admin
│   │   ├── 📁 chat
│   │   ├── 📁 chatNote
│   │   ├── 📁 classRoom
│   │   ├── 📁 common
│   │   ├── 📁 main
│   │   ├── 📁 member
│   │   ├── 📁 notice
│   │   └── 📁 order
│   ├── 📁 resources
│   │   ├── 📁 mappers
│   │   ├── 📁 spring
│   │   │   └── root-context.xml
│   │   ├── log4j.xml
│   │   └── mybatis-config.xml
│   └── 📁 webapp
│       ├── 📁 resources
│       │   ├── 📁 css
│       │   ├── 📁 fontello
│       │   ├── 📁 icon
│       │   ├── 📁 images
│       │   └── 📁 js
│       └── 📁 WEB-INF
│           ├── 📁 lib
│           ├── 📁 views
│           │   ├── 📁 admin
│           │   ├── 📁 chat
│           │   ├── 📁 class
│           │   ├── 📁 common
│           │   ├── 📁 main
│           │   ├── 📁 member
│           │   ├── 📁 notice
│           └── web.xml
├── pom.xml
└── README.md
```
