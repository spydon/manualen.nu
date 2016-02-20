package nu.manualen.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Manualen implements EntryPoint {

    /**
     * Create a remote service proxy to talk to the server-side perk
     * service.
     */
    private final PerkServiceAsync perkService = GWT
            .create(PerkService.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        FlowPanel p = new FlowPanel();
        FlowPanel bgPanel = new FlowPanel();
        final Background background = new Background("/images/nebula.jpg");
        Image logo = new Image("./images/logo.svg");
        logo.addStyleName("logo");
        logo.addClickHandler(new ClickHandler() {
            int timesClicked = 0;

            @Override
            public void onClick(ClickEvent event) {
                timesClicked++;
                if (timesClicked % 5 == 0) {
                    final DialogBox popup = new DialogBox(false, false);
                    Image pic = new Image("/images/gruppen/pic" + (int) ((Math.random()*6)+1) + ".jpg");
                    pic.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            popup.hide();
                        }
                    });
                    popup.add(pic);
                    popup.center();
                }
            }
        });
        ProgressBar progress = new ProgressBar(0, 50000);
        Label name = new Label("Manualen v11111011111");
        Label ticketsHeader = new Label("Biljetter");
        FlowPanel perks = new FlowPanel();
        FlowPanel tickets = new FlowPanel();
        final Button lightSwitch = new Button("Off");
        lightSwitch.addStyleName("light-switch");

        name.addStyleName("h1");
        ticketsHeader.addStyleName("h1");
        bgPanel.addStyleName("bottom-background");
        p.addStyleName("container");
        perks.addStyleName("perks-panel");
        tickets.addStyleName("perks-panel");

        HTML text = new HTML(
                "Året var 1994. De första manualerna hade precis kommit till, handgjorda av forna väsen "
                + "som idag är bortglömda av den gemene datavetarynglingen. Om än dessa personer hade förstått "
                + "vilken process de hade satt i rullning, en sångbok som aktivt används i 20 år efter dess uppkomst. "
                + "<br /><br />Nu är Manualen slut och nya behöver tryckas, för första gången sedan 2001 (om man inte räknar med Jubileums-manualen). "
                + "Men för att det ska bli av så <b>behöver vi din hjälp</b> i form av en donation. Det fungerar så att du donerar och skriver in "
                + "dina uppgifter och när boken är klar så får du alla saker upp till den nivå som du donerat på posten eller i FooBar. "
                + "Så donerar du t.ex. 200kr så får du en Manual i både fysisk och digital form samt ett märke (biljetter köpes dock var för sig). "
                + "Eftersom märkena är väldigt ändliga i antal så får man bara ett märke totalt, dvs om du t.ex. beställt både manualen och ska gå på "
                + "overallssittningen. <br />"
                + "Maila <a href='mailto:dv-kassor@utn.se'>dv-kassor@utn.se</a> om du har några frågor!");
        text.addStyleName("intro-text");

        perks.add(createPerk("/images/patch.png", "Märke<br /> Limited Edition™", 50, 100));
        perks.add(createPerk("/images/ebook.png", "Manualen i Digital Version", 100, 100));
        perks.add(createPerk("/images/manualen.png", "Manualen v11111011111", 200, 100));
        perks.add(createPerk("/images/namnd.png", "Nämnd i boken", 500, 20));
        perks.add(createPerk("/images/sang.png", "Ny sång skriven<br />och tillägnad dig", 1000, 3));
        perks.add(createPerk("/images/halvsida.png", "Egen sida (Ej reklam)", 2000, 4));
        perks.add(createPerk("/images/helsida.png", "Reklam (Helsida)", 10000, 2));
        perks.add(createPerk("/images/fritt.png", "Donera fritt<br />utan att få någonting", -1, 1000));

        tickets.add(createPerk("/images/ticket.png", "Overallssittningen<br />21 November<br />(Märke ingår)", 150, 0));
        tickets.add(createPerk("/images/ticket.png", "Manualsläppsfesten<br />(När den släpps)", 150, 0));

        FlowPanel thanksTo = new FlowPanel();
        HTML thanksHeader = new HTML();
        thanksTo.addStyleName("backers");
        thanksHeader.setHTML("<h1>Stort tack till de som donerat</h1>");
        thanksTo.add(thanksHeader);
        showBackers(thanksTo);

        HTML information = new HTML("Man kan även <b>donera direkt</b> till Uppsala Datavetares konto 9022-27.137.57 (länsförsäkringar)");

        lightSwitch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (lightSwitch.getText().equals("Off")) {
                    background.setRunning(false);
                    lightSwitch.setText("On");
                } else {
                    background.setRunning(true);
                    lightSwitch.setText("Off");
                }
            }
        });

        p.add(name);
        p.add(progress);
        p.add(text);
        p.add(perks);
        p.add(ticketsHeader);
        p.add(tickets);
        p.add(thanksTo);
        p.add(information);

        bgPanel.add(background);

        RootPanel.get().add(logo);
        RootPanel.get().add(p);
        RootPanel.get().add(bgPanel);
        if (Canvas.isSupported()) {
            RootPanel.get().add(lightSwitch);
        }

        Label codedBy = new Label("Kodad av Lukas Klingsbo");
        codedBy.addStyleName("coded-by");
        RootPanel.get().add(codedBy);
        updateProgress(progress);
    }

    private void showBackers(final FlowPanel thanksTo) {
        final HTML backers = new HTML("");
        perkService.getBackers(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(ArrayList<String> backersList) {
                for (String backer : backersList) {
                    if (backers.getHTML().equals("")) {
                        backers.setText(backer);
                    } else {
                        backers.setText(backers.getText() + ", " + backer);
                    }
                }
                thanksTo.add(backers);
            }
        });
    }

    private void updateProgress(final ProgressBar progress) {
        perkService.getAmount(new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(Integer result) {
                progress.setProgress(result);
            }
        });
    }

    private FlowPanel createPerk(String picture, String description, int cost, int qty) {
        FlowPanel row = new FlowPanel();
        Image image = new Image(picture);
        image.addStyleName("perk-image");
        HTML desc = new HTML(description);
        desc.addStyleName("perk-description");
        FlowPanel buttonContainer = new FlowPanel();
        buttonContainer.addStyleName("perk-button-container");
        Button claimB = new Button("Bidra " + (cost == -1 ? "valfri<br />summa" : "med <br />" + cost + "kr"));
        claimB.addStyleName("perk-button");
        buttonContainer.add(claimB);
        Label qtyL = new Label(qty +  " kvar");
        qtyL.addStyleName("perk-qty");
        updateQty(qtyL, claimB, description);

        PayHandler handler = new PayHandler(cost, description);
        image.addClickHandler(handler);
        claimB.addClickHandler(handler);

        row.add(image);
        row.add(desc);
        row.add(buttonContainer);
        row.add(qtyL);

        row.setStyleName("perk-row");

        return row;
    }

    private void updateQty(final Label qtyL, final Button claimB, String perk) {
        perkService.getQty(perk, new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(Integer result) {
                qtyL.setText(result + " kvar");
                if(result < 1) {
                    claimB.setEnabled(false);
                }
            }
        });
    }

    private class PayHandler implements ClickHandler {
        private int total = 0;
        private final DialogBox popup = new DialogBox(true, false);
        private final FlowPanel content = new FlowPanel();
        private final HTML contentHeader = new HTML();
        private final TextBox nameBox = new TextBox();
        private final TextBox addressBox = new TextBox();
        private final TextBox postNumberBox = new TextBox();
        private final TextBox townBox = new TextBox();
        private final TextBox nickBox = new TextBox();
        private final TextBox emailBox = new TextBox();
        private final TextBox preferencesBox = new TextBox();

        public PayHandler(final int total, final String perk) {
            this.total = total;
            FlexTable infoPanel = new FlexTable();

            nickBox.getElement().setAttribute("placeholder", "Tomt för vanligt namn");
            final CheckBox mentioned = new CheckBox();
            mentioned.setValue(true);

            popup.setText("Betala");
            content.addStyleName("popup-content");
            content.add(contentHeader);

            FlowPanel mentionedPanel = new FlowPanel();
            Label mentionedLabel = new Label("Nämnd på sidan");
            mentionedLabel.addStyleName("center");
            mentionedPanel.add(mentionedLabel);
            mentionedPanel.add(mentioned);

            infoPanel.setWidget(0, 0, new Label("Namn"));
            infoPanel.setWidget(0, 1, nameBox);
            infoPanel.setWidget(1, 0, new Label("Adress"));
            infoPanel.setWidget(1, 1, addressBox);
            infoPanel.setWidget(2, 0, new Label("Postnummer"));
            infoPanel.setWidget(2, 1, postNumberBox);
            infoPanel.setWidget(3, 0, new Label("Postort"));
            infoPanel.setWidget(3, 1, townBox);
            infoPanel.setWidget(4, 0, new Label("Nick"));
            infoPanel.setWidget(4, 1, nickBox);
            infoPanel.setWidget(5, 0, new Label("E-mail"));
            infoPanel.setWidget(5, 1, emailBox);
            infoPanel.setWidget(6, 1, mentionedPanel);

            FlowPanel instructions = new FlowPanel();
            final int id = generateId();

            // XXX: LOL FULHACK
            if(total == 150) {
                preferencesBox.getElement().setAttribute("placeholder", "Veg/Allergier etc");
                infoPanel.setWidget(7, 0, new Label("Preferenser"));
                infoPanel.setWidget(7, 1, preferencesBox);
                instructions.add(new HTML("Överför " + total + "kr till 9022-27.137.57 (länsförsäkringar) "
                        + "med " + id + " som meddelande och så får du din biljett via e-mail när det börjar "
                                + "närma sig."));
            } else if (total > 0) {
                instructions.add(new HTML("Överför " + total + "kr till 9022-27.137.57 (länsförsäkringar) "
                                        + "med " + id + " som meddelande och du får det du har valt fritt "
                                                + "levererat till dig (eller hämtat i FooBar)"));
            } else {
                instructions.add(new HTML("Överför till 9022-27.137.57 (länsförsäkringar) "
                        + "med " + id + " som meddelande så får du ditt namn/nick på sidan (om du kryssade i det)"));
            }

            Button closeB = new Button("Stäng");
            closeB.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    popup.hide();
                }
            });

            Button sendB = new Button("Skicka");
            final ArrayList<TextBox> faulty = new ArrayList<TextBox>();
            sendB.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Iterator<TextBox> it = faulty.iterator();
                    while (it.hasNext()) {
                       it.next().removeStyleName("faulty-input");
                       it.remove();
                    }

                    if (nameBox.getText().equals("")) {
                        nameBox.addStyleName("faulty-input");
                        faulty.add(nameBox);
                    }
                    if (addressBox.getText().equals("")) {
                        addressBox.addStyleName("faulty-input");
                        faulty.add(addressBox);
                    }
                    String postalNumber = postNumberBox.getText().replaceAll(" ", "");
                    if (postalNumber.equals("") || !tryParseInt(postalNumber)) {
                        postNumberBox.addStyleName("faulty-input");
                        faulty.add(postNumberBox);
                    }
                    if (townBox.getText().equals("")) {
                        townBox.addStyleName("faulty-input");
                        faulty.add(townBox);
                    }
                    if (emailBox.getText().equals("") || !isValidEmail(emailBox.getText())) {
                        emailBox.addStyleName("faulty-input");
                        faulty.add(emailBox);
                    }

                    if (faulty.size() == 0) {
                        popup.hide();
                        addBacker(id,
                                nameBox.getText(),
                                addressBox.getText(),
                                postNumberBox.getText(),
                                townBox.getText(),
                                nickBox.getText().equals("") ? nameBox.getText() : nickBox.getText(),
                                emailBox.getText(),
                                preferencesBox.getText(),
                                perk,
                                total,
                                mentioned.getValue());
                    }
                }
            });

            content.add(infoPanel);
            content.add(instructions);
            content.add(sendB);
            content.add(closeB);
            popup.add(content);
        }
        @Override
        public void onClick(ClickEvent event) {
            if (total > 0) {
                contentHeader.setText("Du vill alltså bidra med " + total + "kr, vad roligt!");
            } else {
                contentHeader.setText("Du vill alltså donera, man tackar!");
            }
            popup.center();
        }
    }

    private int generateId() {
        int id = 1000000000 + (int) (Math.random()*1000000000);
        return id;
    }

    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }

    private static final RegExp rfc2822 = RegExp.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );

    private static boolean isValidEmail(String email) {
        email = email.toLowerCase();
        if (rfc2822.exec(email) == null) {
            return false;
        }
        return true;
    }

    private void addBacker(int id, String name, String address, String postNumber, String town, String nick, String email, String pref, String perk, int amount, boolean mentioned) {
        final DialogBox popup = new DialogBox(true, false);

        Button closeB = new Button("Stäng");
        closeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.hide();
            }
        });

        popup.add(closeB);

        perkService.addBacker(id, name, address, postNumber, town, nick, email, pref, perk, amount, mentioned, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                popup.setText("Tack för hjälpen till den nya Manualen!");
                popup.center();
            }

            @Override
            public void onFailure(Throwable caught) {
                popup.setText(caught.getMessage());
                popup.center();
            }

        });
    }
}
