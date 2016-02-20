package nu.manualen.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ProgressBar extends Composite {
    private static final String PERCENT_PATTERN = "#,##0%";
    private static final NumberFormat percentFormat = NumberFormat.getFormat(PERCENT_PATTERN);

    private final FlowPanel p;
    private final Label info;
    private double percentage;
    private final int max;
    private final Bar bar;

    public ProgressBar(int value, int max) {
        this.max = max;
        this.p = new FlowPanel();
        this.info = new Label();

        bar = new Bar();
        bar.setProgress(value);

        p.add(bar);
        p.add(info);


        initWidget(p);
    }

    public void setProgress(int value) {
        bar.setProgress(value);
    }

    public void updateInfo(int value) {
        info.setText(value + "kr av " + max + "kr (" + percentFormat.format((double) value/ (double) max) + ")");
        info.addStyleName("progress-label");
    }

    private class Bar extends Widget {
        private final Element progress;
        private final Element percentageLabel;

        public Bar() {
            progress = DOM.createElement("progress");
            percentageLabel = DOM.createElement("span");
            progress.setAttribute("max", Double.toString(max));
            progress.insertFirst(percentageLabel);
            setElement(progress);
        }

        public void setProgress(int value) {
            progress.setAttribute("value", Double.toString(value));
            percentage = value / max;
            percentageLabel.setInnerHTML(percentFormat.format(percentage));
            updateInfo(value);
        }
    }
}