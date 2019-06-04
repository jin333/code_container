title : 설계패턴 과제 ass 05
date : 2019-06-05

 - OS		: window 
 - Version : Java 8
 - JAR	
	- commons-math3-3.6.1.jar
		
Description 
-----------

 - For문으로 구현되어 있던 자바 프로그램을 Stream을 이용하여 처리
 - 기존의 For문을 이용하는 경우, Stream을 이용한 경우의 어떤한 차이가 있는지 확인


Conclusion
----------

 - Stream을 이용하는 경우 for문을 이용하는 것보다 빠른 성능을 보였다
  [ 9.54s -> 5.62S로 처리 속도에 개선 ]
 - Collection을 Stream으로, Stream을 자체적으로 만들어낸 방법을 확인하였다.
 - Stream을 처리하는map/peek/foreach 등에서 사용되는 interface와 구현 방법을 알 수 있었다.