Spring Security 사용해보기
-

> Spring Security는 `Spring Security Session`을 들고 있다.
> 
> 그러면 원래 서버 세션 영역 안에 시큐리티가 관리하는 세션이 따로 존재하게 된다.
> 
> 시큐리티 세션에는 `Authentication` 객체만 들어갈 수 있다.
> 
> `Authentication`가 시큐리티 세션 안에 있다면 로그인된 상태를 의미한다.
> 
> `Authentication`에는 2개의 타입이 들어갈 수 있는데 `UserDetails`, `OAuth2User`이다.
> 
> ### 그렇다면 ??
> 
> 2개의 세션 타입 -> 컨트롤러에서 처리하기 복잡해는 문제점 발생 !!
> 
> 일반 로그인 -> `UserDetails` 타입으로 `Authentication` 객체가
> 
> (구글 로그인) OAuth 로그인 -> `OAuth2User` 타입으로 `Authentication` 객체가 만들어지기 때문이다.
> 
> ### 해결방법 :
> 
> `PrincipalDetails`에 `UserDetails`, `OAuth2User`를 implements한다.
> 
> 이제부터는 `PrincipalDetails`만 사용하면 된다.