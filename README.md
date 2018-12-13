<p align="center"><img src="https://github.com/wholder/MarkupView/blob/master/images/MarkupView%20Screenshot.png"></p>

## MarkupView

I wrote `MarkupView` to give me a way to include nicely-formatted documentation in my Java apps by including files in the ["**MarkDown**"](https://en.wikipedia.org/wiki/Markdown) format in the resource folder in a .jar file.  `MarkupView` makes use of a markdown to HTML converter called [**TxtMark**](https://github.com/rjeschke/txtmark), which was written by René Jeschke.  My code adds to this the ability to load images and pages written in MarkDown from the resource folder and also follow links other MarkDown pages in the resource folder as well as links to external sites by invoking the default browser.  Files are displayed by means of a JTestEditor component using HTMLKit inside a JScrollPane.  A minimal set of CSS styles is also defined in the code to format the HTML output.  However, I've only implemented the formatting features I needed, so you may need to expand on this for your application.

You can try out the program just by downloading the pre-built, executable JAR file in the [out/artifacts/MarkupView_jar](https://github.com/wholder/MarkupView/tree/master/out/artifacts/MarkupView_jar) folder.  This display some sample MarkDown text like that shown in the image above.

### Notes
 * Yeah, I should have named it MarkdownView, but I'd already started the project as `MarkupView` and it had spread to other projects before I realized my mistake.
 * Making a `jEditorPane` load images and content links from the resources path took some creative subclassing and overriding of the Java classes `HTMLEditorKit`, `ViewFactory` and `ImageView` (see source code), but this was the only way I could figure out how to make this work.  If anyone knows of a cleaner way to do this, please enlighten me.
 
### Requirement

`MarkupView` requires Java 8 JRE or [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), or later to be installed.

## Credit and Thanks
This project would have been much harder and much less cool without help from the following open source projects, or freely available software.
- [TxtMark](https://github.com/rjeschke/txtmark) is used as a speedy way to convert MarkDown to HTML.
- [IntelliJ IDEA from JetBrains](https://www.jetbrains.com/idea/) (my favorite development environment for Java coding. Thanks JetBrains!)

### License

I'm publishing this source code under the MIT License (See: https://opensource.org/licenses/MIT)