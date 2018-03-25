#!/bin/bash
echo "Converting markdown tests (tests/markdown/XX.txt) to html (tests/html/XX.html)"
echo ""
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/00.txt -o ../../../tests/outputs/html/00.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/01.txt -o ../../../tests/outputs/html/01.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/02.txt -o ../../../tests/outputs/html/02.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/03.txt -o ../../../tests/outputs/html/03.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/04.txt -o ../../../tests/outputs/html/04.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/05.txt -o ../../../tests/outputs/html/05.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/06.txt -o ../../../tests/outputs/html/06.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/07.txt -o ../../../tests/outputs/html/07.html
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/08.txt -o ../../../tests/outputs/html/08.html
echo ""
echo "Conversion completed."
echo ""
echo ""
echo "Running acceptance test"
echo ""
java -jar SWEN301-Project-1.jar -i ../../../tests/markdown/acceptanceTest.txt -o ../../../tests/outputs/html/acceptanceTest.html
echo ""
echo "Acceptance test complete."
echo ""
echo ""
echo "Running JUnit tests"
echo ""
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_01
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_02
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_03
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_04
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_05
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_06
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_07
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_08
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Paragraph_Slicing_09
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_00
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_01
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_02
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_03
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_04
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_Bad_01
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_Bad_02
java -cp SWEN301-Project-1.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar RunJunitFromCLI MDParser_Tests#Test_Heading_Regex_Bad_03
echo ""
echo JUnit tests complete
echo ""
