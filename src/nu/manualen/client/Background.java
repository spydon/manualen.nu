package nu.manualen.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

public class Background extends Composite {

    private final Canvas canvas, canvasBuffer;
    private final Context2d context, contextBuffer;
    private double width, height;
    private ImageElement image;
    private boolean running = true;

    public Background(String imageUrl) {
        if (!Canvas.isSupported()) {
            running = false;
        }
        canvas = Canvas.createIfSupported();
        canvasBuffer = Canvas.createIfSupported();
        context = canvas.getContext2d();
        contextBuffer = canvasBuffer.getContext2d();
        canvas.setHeight("100%");
        canvas.setWidth("100%");
        canvasBuffer.setHeight("100%");
        canvasBuffer.setWidth("100%");
        final Image imageSrc = new Image(imageUrl);

        imageSrc.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                image = ImageElement.as(imageSrc.getElement());
                drawImage();
            }
        });
        FlowPanel loadingPanel = new FlowPanel();
        loadingPanel.add(imageSrc);
        loadingPanel.setStyleName("loading-panel");
        RootPanel.get().add(loadingPanel);
        FlowPanel canvasContainer = new FlowPanel();
        canvasContainer.addStyleName("canvas-container");
        canvasContainer.add(canvas);
        canvasContainer.add(canvasBuffer);
        canvas.setStyleName("background-canvas");
        canvasBuffer.setStyleName("background-canvas");
        initWidget(canvasContainer);
        setStyleName("background-canvas");
    }

    private void drawImage() {
        width = canvas.getOffsetWidth();
        height = canvas.getOffsetHeight();
        initTransform(canvas, context);
        Timer t = new Timer() {
            @Override
            public void run() {
                initTransform(canvasBuffer, contextBuffer);
            }
        };
        t.schedule(250);
    }

    private void transform(final Canvas canvas, final Context2d context, final int speed, final double[] coords, final int opacity, final int iteration) {
        context.save();
        coords[4] -= speed;
        coords[5] -= speed;
        coords[6] += speed;
        coords[7] += speed;
        context.drawImage(image,
                coords[0], coords[1], coords[2], coords[3],
                coords[4], coords[5], coords[6], coords[7]);
        canvas.getElement().getStyle().setOpacity(opacity/100.0);
        context.setGlobalAlpha(opacity/100.0);
        context.restore();

        final Timer t = new Timer() {
            @Override
            public void run() {
                int opacityChange = iteration < 50 ? 2 : -2;
                transform(canvas, context, speed, coords, opacity+opacityChange, iteration+1);
            }
        };
        if(iteration >= 100) {
            initTransform(canvas, context);
        } else {
            t.schedule(50);
        }
    }

    public void setRunning(boolean running) {
        if (!this.running && running) {
            drawImage();
        }
        this.running = running;
    }

    private boolean isSlow = false;
    private void initTransform(final Canvas canvas, final Context2d context) {
        double sx = Math.random()*200;
        double sy = Math.random()*200;
        double sw = sx+(width/height)*400;
        double sh = sy+(height/width)*400;
        double args1[] = {sx, sy, sw, sh, 0, 0, width, height};
        isSlow = !isSlow;
        if (running) {
            transform(canvas, context, isSlow ? 1 : 2, args1, 0, 0);
        }
    }
}
