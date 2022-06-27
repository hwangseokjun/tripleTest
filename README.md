# 트리플 과제 제출 ( 2022. 06. 27 )

## 📌작업환경

<img src="https://img.shields.io/badge/JAVA 8-orange?style=flat&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/Springboot-6DB33F?style=flat&logo=Springboot&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=flat&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1??style=flat&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/intelliJ-red?style=flat&logo=IntelliJ+IDEA&logoColor=white"> <img src="https://img.shields.io/badge/Github-black?style=flat&logo=github&logoColor=white">
<br>

## 📌스키마

![Untitled](https://user-images.githubusercontent.com/45547558/175925285-4d1420ff-a464-46ba-b4e5-421759e41feb.png)
<br>

## 📌DDL

![DDL](https://user-images.githubusercontent.com/45547558/175923098-a6337a5d-b6a0-43d2-8f73-70e4c9f4410d.png)
<br>

## 📌API 명세서

|기능|URL|METHOD|REQUEST|RESPONSE|
|:---:|---|:----:|-------|--------| 
|이벤트 등록|/event|`POST`|{ <br>**"type"**: "REVIEW",  <br>**"action"**: "ADD", /* "MOD", "DELETE" */  <br>**"reviewId"**: "240a0658-dc5f-4878-9381-ebb7b2667772",  <br>**"content"**: "좋아요!",  <br>**"attachedPhotoIds"**: ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],  <br>**"userId"**: "3ede0ef2-92b7-4817-a5f3-0c575361f745",  <br>**"placeId"**: "2e4baf1c-5acb-4efb-a1af-eddada31b00f" <br>}|{ <br>**"type"**: "REVIEW",  <br>**"action"**: "ADD", /* "MOD", "DELETE" */  <br>**"reviewId"**: "240a0658-dc5f-4878-9381-ebb7b2667772",  <br>**"content"**: "좋아요!",  <br>**"attachedPhotoIds"**: "[e4d1a64e-a531-46de-88d0-ff0ed70c0bb8, afb0cef2-851d-4a50-bb07-9cc15cbdc332]",  <br>**"userId"**: "3ede0ef2-92b7-4817-a5f3-0c575361f745",  <br>**"placeId"**: "2e4baf1c-5acb-4efb-a1af-eddada31b00f" <br> **"point"**: 3 <br>}|
|사용자별 이벤트 총점 계산|/event/{userId}|`GET`||{<br>**"point"**: 100 <br>}|

<br>

## 📌Application 사용법

명세서를 토대로 API에 요청을 보낼 시, 응답값을 보냅니다.
