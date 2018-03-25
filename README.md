## How to run:

#### Running on an ECS/Windows Machine:

1. You will need to git clone the repo. I have added everyone as members that needs to be, hopefully this should be enough? I don't know if you have the ability to add your own SSH keys... Good Luck
2. Once cloned, open terminal (tcsh) and cd into the repo and then cd further to: /out/artifacts/SWEN301_Project_1_jar
3. You should see the jar file, a .bat script and a .sh script. To run the .sh file we need to make it executable, so type:
    chmod 755 runme_linux.sh
4. Once this has completed, type ./runme_linux.sh. With any luck, this work have worked fine and will run everything.

#### Running / Testing summary

As a summary, what running this has done is run all my .txt integration tests, my acceptance test (all located in tests/markdown) and put it through my parser and spat it all out in the outputs/html folder.
I never got as far to output to LaTeX/ASCII otherwise this would've put it in the latex/ascii folders respectively.
I also made it run my internal JUnit tests (which are in MDParserTests.java if you wish to inspect them).

If you want to check the outputs are 'correct' in HTML you will have to compare the .txt file markdown input vs .html source. I made all of the inputs vs outputs the same name to make this easier for you. I.E:

tests/markdown/00.txt -> tests/outputs/html/00.html

tests/markdown/01.txt -> tests/outputs/html/01.html

tests/markdown/02.txt -> tests/outputs/html/02.html

ETC.

#### Additional Testing

If you have some markdown tests of your own you wish to run, all you need to do is open the runme_linux.sh file and add new lines to it.

If the format of the command line parameters for the jar aren't self explanatory enough, you can run this command (in out/artifacts/SWEN301_Project_jar dir):
> java -jar SWEN301-Project-1.jar --help

Currently, adding an output of .tex or .ascii won't do anything as I didn't get that far in the assignment.

## What I did:

* Task 1 (HTML Output), 2 (Paragraphs), 5 (Headings), ran out of time for anything else.
* Correct regex's for mixing and matching different the different blocks I implemented within eachother. See tests/acceptance tests.
* Created a recursive descent parser that generates a AST (Abstract Syntax Tree). This is then converted to an Output type.
* Simple methodology (modified strategy pattern) for outputting to different markup outputs (HTML, ASCII, LaTeX). Easily extensible.
* I focused on code quality with good feature implementation quality as opposed to implementing things badly with edge cases not handled well.
* I tried to use knowledge from SWEN222 and other courses, I spent a lot of time designing, planning, refactoring and improving my code.

In summary, I'm happy with what I achieved. I think I shouldn't have deliberated for so long on my design plan but it's probably good experience.
The reason why I wanted to focus on good code quality as opposed to focusing on a feature rich implementation was because,
I took this assignment as a challenge; not because it was conceptually difficult; but because it was difficult to get right.
So I think putting effort into implementing it 'right' is where the real time sink came into it.

## PEG (Markdown Grammar):

Grammar I loosely designed and loosely followed as a structure:

Document =  [Block]+

Block = ContainerBlock | LeafBlock | InLine

ContainerBlock = Block | BlockQuote | List

List = BulletList | OrderedList

LeafBlock = HorizontalRule | Header | Paragraph | BlankLine | FencedCodeBlock

InLine = CodeSpan | Bold | Italic | HardLineBreak | SoftLineBreak | Text

## Summary and Thoughts:

If I wished to implement the whole markdown parser in the future, I believe I'm in a good position to do that with the framework I've developed.
But in saying that (at least for me and my development speed), to implement it well and to complete all 14 tasks I think would take way too long. Longer than 2 weeks at least.
I think next year if this was made a 40% assignment it would be better suited (I'm guessing the 40% assignments have a longer due period).

I think as it's the first time the assignment has been run I just wanted to brief why I chose to do what I did/didn't do in the hopes it
will help next year. I think personally it was a brutal assignment if you wanted to do it correctly and totally wasn't viable to finish in the time constraints
if you followed my methodology... Maybe I'm just slow. I digress.

## References:

Using this as a reference to generate correct markdown regex's and test cases
> http://spec.commonmark.org/0.28/

Used this page to help with regex's:
> https://github.com/markedjs/marked/blob/master/lib/marked.js
