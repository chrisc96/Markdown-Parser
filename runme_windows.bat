@ECHO OFF
echo Converting markdown tests (tests/markdown/XX.txt) to html (tests/html/XX.html)
echo working...
java -jar SWEN301-Project-1.jar -i tests/markdown/00.txt -o tests/html/00.html
java -jar SWEN301-Project-1.jar -i tests/markdown/01.txt -o tests/html/01.html
java -jar SWEN301-Project-1.jar -i tests/markdown/02.txt -o tests/html/02.html
java -jar SWEN301-Project-1.jar -i tests/markdown/03.txt -o tests/html/03.html
java -jar SWEN301-Project-1.jar -i tests/markdown/04.txt -o tests/html/04.html
java -jar SWEN301-Project-1.jar -i tests/markdown/05.txt -o tests/html/05.html
java -jar SWEN301-Project-1.jar -i tests/markdown/06.txt -o tests/html/06.html
java -jar SWEN301-Project-1.jar -i tests/markdown/07.txt -o tests/html/07.html
java -jar SWEN301-Project-1.jar -i tests/markdown/08.txt -o tests/html/08.html
echo Conversion completed.
echo.
echo.
echo Running acceptance test (html result located in tests/html)
java -jar SWEN301-Project-1.jar -i tests/markdown/acceptanceTest.txt -o tests/html/acceptanceTest.html
echo Acceptance test complete.
echo.
echo The JUnit tests I wrote are located in class 'MDParserTests.java' if you wish to view them. Unzip the jar file.
echo.
cmd /k