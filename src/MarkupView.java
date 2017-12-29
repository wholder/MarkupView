import com.github.rjeschke.txtmark.Processor;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

class MarkupView extends JPanel {
  private JEditorPane         jEditorPane;
  private ArrayList<String>   stack = new ArrayList<>();
  private String              currentPage;

  static class MyImageView extends ImageView {
    String loc;

    private MyImageView (Element elem) {
      super(elem);
      try {
        loc = URLDecoder.decode((String) elem.getAttributes().getAttribute(HTML.Attribute.SRC), "UTF-8");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
      }
    }

    @Override
    public URL getImageURL () {
      return getClass().getResource(loc);
    }
  }

  static class MyViewFactory implements ViewFactory {
    ViewFactory view;

    private MyViewFactory (ViewFactory view) {
      this.view = view;
    }

    public View create (Element elem) {
      AttributeSet attrs = elem.getAttributes();
      Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
      Object obj = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
      if (obj instanceof HTML.Tag && obj == HTML.Tag.IMG) {
        return new MyImageView(elem);
      }
      return view.create(elem);
    }
  }

  static class MyEditorKit extends HTMLEditorKit {
    @Override
    public ViewFactory getViewFactory () {
      return new MyViewFactory(super.getViewFactory());
    }
  }

  MarkupView (String markup) throws IOException {
    setLayout(new BorderLayout());
    jEditorPane = new JEditorPane();
    JScrollPane scrollPane = new JScrollPane(jEditorPane);
    JButton back = new JButton("BACK");
    jEditorPane.addHyperlinkListener(ev -> {
      if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        String link = ev.getURL().toString();
        if (link.startsWith("file://")) {
          stack.add(currentPage);
          loadPage(link.substring(7));
          back.setVisible(stack.size() > 0);
        } else {
          if (Desktop.isDesktopSupported()) {
            try {
              Desktop.getDesktop().browse(new URI(link));
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }
      }
    });
    jEditorPane.setEditable(false);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    add(scrollPane, BorderLayout.CENTER);
    back.addActionListener(e -> {
      if (stack.size() > 0) {
        String prev = stack.remove(stack.size() - 1);
        loadPage(prev);
        jEditorPane.setCaretPosition(0);
        back.setVisible(stack.size() > 0);
      }
    });
    add(back, BorderLayout.NORTH);
    back.setVisible(false);
    HTMLEditorKit kit = new MyEditorKit();
    // Setup some basic markdown styles
    StyleSheet styleSheet = kit.getStyleSheet();
    styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
    styleSheet.addRule("h1 {font-size: 32px; font-weight: 600;}");
    styleSheet.addRule("h2 {font-size: 24px; font-weight: 600;}");
    styleSheet.addRule("h3 {font-size: 20px; font-weight: 600;}");
    styleSheet.addRule("h4 {font-size: 16px; font-weight: 600;}");
    styleSheet.addRule("h5 {font-size: 14px; font-weight: 600;}");
    styleSheet.addRule("h6 {font-size: 12px; font-weight: 600;}");
    styleSheet.addRule("pre {margin-left: 0.5cm}");
    styleSheet.addRule("ol {margin-left: 1cm;}");
    styleSheet.addRule("li {font-size: 12px;}");
    styleSheet.addRule("code {font-size: 12px; margin-bottom: 3px;}");
    styleSheet.addRule("p {font-size: 14px; margin-top: 5px; margin-bottom: 5px;}");
    jEditorPane.setEditorKit(kit);
    loadPage(markup);
  }

  private void loadPage (String loc) {
    try {
      jEditorPane.setText(Processor.process(new String(getResource(loc))));
      currentPage = loc;
      jEditorPane.setCaretPosition(0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private byte[] getResource (String file) throws IOException {
    InputStream fis = getClass().getResourceAsStream(file);
    if (fis != null) {
      byte[] data = new byte[fis.available()];
      fis.read(data);
      fis.close();
      return data;
    }
    throw new IllegalStateException("MarkupView.getResource() " + file + " not found");
  }

  public static void main (String[] args) throws IOException {
    JFrame frame = new JFrame();
    MarkupView mView = new MarkupView("demopage1.md");
    frame.add(mView, BorderLayout.CENTER);
    frame.setSize(850, 1024);
    frame.setVisible(true);
  }
}
