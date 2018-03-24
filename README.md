## What I did:

* Task 1, 2, 5.
* Correct regex's for mixing and matching different the different blocks I implemented (2,5) within eachother. See tests/acceptance tests.
* Created a recursive descent parser that generates a AST (Abstract Syntax Tree). This is then converted to an Output type.
* Simple methodology (modified strategy pattern) for outputting to different markup outputs (HTML, ASCII, LaTeX). Easily extensible.
* I focused on code quality with good feature implementation quality as opposed to implementing things badly with edge cases not handled well.
* I tried to use knowledge from SWEN222 and other courses, I spent a lot of time designing, planning, refactoring and improving my code.

In summary, I'm happy with what I achieved. I think I shouldn't have deliberated for so long on my design plan but it's probably good experience.
The reason why I wanted to focus on good code quality as opposed to focusing on a feature rich implementation was because,
I took this assignment as a challenge; not because it was conceptually difficult; but because it was difficult to get right.
So I think putting effort into implementing it 'right' is where the real time comes into it. For instance, all the edge cases in the regex's,
the esoteric nature of whitespacing and so much more. 

## Summary and Thoughts

If I wanted to implement the whole markdown parser I think I'm in a good position to do that with the framework I've developed.
But in saying that (at least for me and my development speed), to implement it well and to complete all 14 tasks I think would take way too long. Longer than 2 weeks at least.
I think next year if this was made a 40% assignment it would be better suited (I'm guessing the 40% assignments have a longer due period).

I think as it's the first time the assignment has been run I just wanted to brief why I chose to do what I did/didn't in the hopes it
will help next year. I think personally it was a brutal assignment if you want to do it correctly and totally not viable to finish in the time constraints
if you followed my methodology...



Using this as a reference to generate correct markdown regex's and test cases
> http://spec.commonmark.org/0.28/

Used this page to help with regex's:
> https://github.com/markedjs/marked/blob/master/lib/marked.js


Grammar I will likely be following (as of 18/03):

Document =  [Block]+

Block = ContainerBlock | LeafBlock | InLine

ContainerBlock = Block | BlockQuote | List

List = BulletList | OrderedList

LeafBlock = HorizontalRule | Header | Paragraph | BlankLine | FencedCodeBlock

InLine = CodeSpan | Bold | Italic | HardLineBreak | SoftLineBreak | Text