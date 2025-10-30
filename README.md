# 📱 GURU Project  
**Android / Kotlin 기반 독서 기록 애플리케이션 개발 프로젝트**

---

## 📌 Overview  
**GURU 프로젝트**는 안드로이드 앱 개발 실무 역량 강화를 목표로 진행된 팀 프로젝트입니다.  
사용자가 읽은 책을 등록·조회·수정·삭제하고 메모를 기록할 수 있는 화면을 구현했습니다.  

SQLite를 이용해 로컬 데이터베이스를 설계하고,  
사용자가 자신의 독서 습관을 쉽게 관리할 수 있도록 직관적이고 단정한 UI를 구축했습니다.  

---

## 🧩 My Role — *Front-end Developer*  
- 앱 화면 기획 및 XML 기반 UI 설계  
- **메인, 책 등록/수정/삭제, 목록, 메모 작성 화면** 구현  
- ConstraintLayout, LinearLayout, ScrollView 등을 활용한 반응형 화면 구성  
- EditText, TextView, Button 등의 스타일 통일 및 색상 일관성 확보  
- 백엔드 연동 테스트 및 GitHub 기반 협업 환경 관리  

---

## ⚙️ Key Features

| 기능 | 설명 | 주요 파일 |
|------|------|-----------|
| 책 기록하기 | 사용자가 읽은 책 정보를 작성하고 등록 | `BookAdd.kt` |
| 책 삭제하기 | 기존에 기록한 책 정보 삭제 | `BookInfo.kt` |
| 책 정보 수정하기 | 등록된 책 정보 수정 | `BookEdit.kt` |
| 책 목록 보기 | 기록한 모든 책의 리스트 조회 | `BookList.kt` |
| 책 표지 업로드 | 갤러리에서 표지 이미지를 업로드 | `BookAdd.kt` |
| 책 메모 작성하기 | 각 책별 감상 메모 작성 및 등록 | `MemoWriting.kt` |

---

## 🛠 Tech Stack

| 분야 | 기술 |
|------|------|
| Language | Kotlin |
| Framework | Android SDK |
| DB | SQLite |
| UI Layout | ConstraintLayout, LinearLayout, ScrollView, RecyclerView |
| Tools | Android Studio, GitHub, Notion |

---

## 🎨 UI Design Highlights

- **Main 화면:** 앱 타이틀, 책 목록, 추가 버튼 배치  
- **책 등록 화면:** 책 제목·작가·출판사·날짜 입력 및 표지 이미지 업로드  
- **책 목록 화면:** 최근순·가나다순 정렬, 썸네일 표시  
- **책 상세 화면:** 표지 이미지·기본 정보·메모 표시  
- **메모 작성 화면:** 감상 메모 작성 및 수정 가능  

---

## 💡 Lessons & Outcomes

- **XML 레이아웃 설계 능력 강화:** ConstraintLayout과 LinearLayout의 구조 차이를 실무 수준으로 익힘  
- **Git 협업 프로세스 경험:** pull/push, 충돌 해결, 브랜치 관리 등 실무형 버전 관리 체계 이해  
- **UI/UX 감각 향상:** 사용자 중심의 인터페이스와 색상 통일성 확보  

---

## 🚀 향후 개선 방향  
- 월별·연도별 독서 통계 시각화 기능 추가  
- 클라우드 동기화를 통한 다중 기기 데이터 연동  
- 사용자 맞춤형 도서 추천 기능 개발  
- Firebase 기반 로그인 및 데이터 백업 기능 확장  

---

## 🔗 Repository  
[👉 GURU Project (GitHub)](https://github.com/hansole27/GURU-Project)
