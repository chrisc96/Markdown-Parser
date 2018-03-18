Using this as a reference to generate correct markdown regex's and grammar structures:
> http://spec.commonmark.org/0.13/

Grammar I will likely be following (as of 18/03):

Document =  [Block]+\
Block = ContainerBlock | LeafBlock | InLine\
ContainerBlock = Block | BlockQuote | List\
List = BulletList | OrderedList\
LeafBlock = HorizontalRule | Header | Paragraph | BlankLine | FencedCodeBlock\
InLine = CodeSpan | Bold | Italic | HardLineBreak | SoftLineBreak