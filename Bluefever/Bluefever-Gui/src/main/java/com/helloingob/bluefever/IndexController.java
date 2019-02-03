package com.helloingob.bluefever;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;

import com.helloingob.bluefever.data.ThermostatAssetDAO;
import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.ThermostatJobDAO;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatJobTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.FontImage;
import com.helloingob.bluefever.util.GuiSettings;
import com.helloingob.bluefever.util.NotificationManager;
import com.helloingob.bluefever.util.RegularComparator;
import com.helloingob.bluefever.util.TimestampComparator;
import com.helloingob.bluefever.util.WindowMaker;

public class IndexController extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = -480476048052566214L;
    private Listbox lbxTemperature;
    private Listbox lbxJobs;

    private Listheader lhdRegular;
    private Listheader lhdJobDate;
    private Listheader lhdTemperatureDate;

    private Hlayout hlDevices;

    private Doublebox dbxTemperature;

    private int current_thermostat_pk;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        dbxTemperature.setValue(22.00);
        initPagings();
        initSorter();
        lhdRegular.appendChild(new FontImage("z-icon-refresh"));
        loadData();
    }

    private void initSorter() {
        lhdJobDate.setSortAscending(new TimestampComparator(true));
        lhdJobDate.setSortDescending(new TimestampComparator(false));

        lhdTemperatureDate.setSortAscending(new TimestampComparator(true));
        lhdTemperatureDate.setSortDescending(new TimestampComparator(false));

        lhdRegular.setSortAscending(new RegularComparator(true));
        lhdRegular.setSortDescending(new RegularComparator(false));
    }

    private void loadData() {
        List<ThermostatTO> thermostats = ThermostatDAO.get();
        Collections.reverse(thermostats);
        addThermostats(thermostats);
    }

    private void initPagings() {
        initPaging(lbxTemperature, 24);
    }

    private void initPaging(Listbox listbox, int pageSize) {
        listbox.setMold("paging");
        listbox.setPageSize(pageSize);
    }

    public void onClick$btnAddJob() {
        if (ThermostatDAO.anyDeviceExists()) {
            WindowMaker.createWindow("job.zul", "Add job", null, false).addEventListener("onListChanged", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    fillJobs();
                }
            });
        } else {
            NotificationManager.showWarning("Add thermostat first!", NotificationManager.CENTER);
        }
    }

    public void onClick$btnEditJob() {
        if (lbxJobs.getSelectedItem() != null) {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("job", lbxJobs.getSelectedItem().getValue());
            WindowMaker.createWindow("job.zul", "Edit job", parameter, false).addEventListener("onListChanged", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    fillJobs();
                }
            });
        } else {
            NotificationManager.showWarning("Select job!", lbxJobs, NotificationManager.RIGHT);
        }
    }

    public void onClick$btnDeleteJob() {
        if (lbxJobs.getSelectedItem() != null) {
            Messagebox.show("Are you sure to delete the selected job?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                public void onEvent(Event event) throws InterruptedException {
                    if (event.getName().equals(Events.ON_OK)) {
                        ThermostatJobTO thermostatJob = (ThermostatJobTO) lbxJobs.getSelectedItem().getValue();
                        ThermostatJobDAO.delete(thermostatJob);
                        fillJobs();
                    }
                }
            });
        } else {
            NotificationManager.showWarning("Select job!", lbxJobs, NotificationManager.RIGHT);
        }
    }

    private void addThermostats(List<ThermostatTO> thermostats) {
        hlDevices.getChildren().clear();

        for (ThermostatTO thermostat : thermostats) {
            Button button = new Button(thermostat.getName());
            button.setHflex("true");
            button.setHeight("100px");
            button.setAttribute("pk", thermostat.getPk());
            button.setStyle("font-size: 40px;");

            button.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    current_thermostat_pk = (Integer) event.getTarget().getAttribute("pk");
                    fillTemperatures();
                    fillJobs();

                    for (Component component : hlDevices.getChildren()) {
                        if (component instanceof Button) {
                            Button otherButton = (Button) component;
                            otherButton.setStyle("font-size: 40px;color:white");
                        }
                    }
                    Button clickedButton = (Button) event.getTarget();
                    clickedButton.setStyle("font-size: 40px;color:red");
                }
            });
            hlDevices.appendChild(button);
            Separator separator = new Separator();
            separator.setWidth("30px");
            hlDevices.appendChild(separator);
        }
        if (thermostats.size() > 0) {
            Button button = (Button) hlDevices.getChildren().get(0);
            Events.postEvent(Events.ON_CLICK, button, null);
        }
    }

    private void fillTemperatures() {
        lbxTemperature.getItems().clear();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (ThermostatAssetTO thermostatAsset : ThermostatAssetDAO.getAssets(current_thermostat_pk)) {
            Listitem listitem = new Listitem();
            listitem.setHeight("100px");

            Listcell listcellDate = new Listcell();
            Label labelDate = new Label(dateTimeFormatter.format(thermostatAsset.getDate().toLocalDateTime()));
            labelDate.setStyle("font-size: 40px;");
            listcellDate.appendChild(labelDate);
            listitem.appendChild(listcellDate);

            Listcell listcellCurrent = new Listcell();
            Label labelCurrent = new Label(formatTemperature(thermostatAsset.getCurrentTemperature()));
            labelCurrent.setStyle("font-size: 40px;");
            listcellCurrent.appendChild(labelCurrent);
            listitem.appendChild(listcellCurrent);

            Listcell listcellManual = new Listcell();
            Label labelManual = new Label(formatTemperature(thermostatAsset.getManualTemperature()));
            labelManual.setStyle("font-size: 40px;");
            listcellManual.appendChild(labelManual);
            listitem.appendChild(listcellManual);

            Listcell listcellOffset = new Listcell();
            Label labelOffset = new Label(formatTemperature(thermostatAsset.getOffsetTemperature()));
            labelOffset.setStyle("font-size: 40px;");
            listcellOffset.appendChild(labelOffset);
            listitem.appendChild(listcellOffset);

            lbxTemperature.appendChild(listitem);
        }
    }

    private String formatTemperature(Double temperature) {
        String appendix = "";
        if (temperature.equals(GuiSettings.MIN_TEMP)) {
            appendix = " (OFF)";
        }
        if (temperature.equals(GuiSettings.MAX_TEMP)) {
            appendix = " (ON)";
        }
        return String.valueOf(temperature) + appendix;
    }

    private void fillJobs() {
        lbxJobs.getItems().clear();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (ThermostatJobTO thermostatJob : ThermostatJobDAO.get(current_thermostat_pk)) {
            Listitem listitem = new Listitem();
            listitem.setHeight("100px");
            listitem.setValue(thermostatJob);

            Listcell listcellActive = new Listcell();
            FontImage fontImageActive = new FontImage("z-icon-check", 30);
            if (thermostatJob.isActive()) {
                listcellActive.appendChild(fontImageActive);
            }
            listitem.appendChild(listcellActive);

            if (thermostatJob.getNextExecution().isBefore(LocalDateTime.now())) {
                listitem.setZclass("pastNow");
            }

            Listcell listcellType = new Listcell();
            if (thermostatJob.getDayOfWeek() != null) {
                Label labelType = new Label(DayOfWeek.of(thermostatJob.getDayOfWeek()).getDisplayName(TextStyle.FULL, Locale.GERMAN));
                labelType.setStyle("font-size: 40px;");
                listcellType.appendChild(labelType);
            }
            listitem.appendChild(listcellType);

            Listcell listcellExecution = new Listcell();
            Label labelExecution = new Label(dateTimeFormatter.format(thermostatJob.getNextExecution()));
            labelExecution.setStyle("font-size: 40px;");
            listcellExecution.appendChild(labelExecution);
            listitem.appendChild(listcellExecution);

            Listcell listcellTemperature = new Listcell();
            Label labelTemperature = new Label(formatTemperature(thermostatJob.getTemperature()));
            labelTemperature.setStyle("font-size: 40px;");
            listcellTemperature.appendChild(labelTemperature);
            listitem.appendChild(listcellTemperature);

            lbxJobs.appendChild(listitem);
        }
        lhdJobDate.sort(true, true);
    }

    public void onClick$btnSet() {
        ThermostatJobTO newThermostatJob = new ThermostatJobTO();
        newThermostatJob.setActive(true);
        newThermostatJob.setTemperature(dbxTemperature.getValue());
        newThermostatJob.setThermostatPk(current_thermostat_pk);
        newThermostatJob.setDate(new Timestamp((new Date(System.currentTimeMillis() + 60 * 1000)).getTime()));
        if (ThermostatJobDAO.add(newThermostatJob)) {
            NotificationManager.showInfo("Temperature successfully added.", NotificationManager.CENTER);
            fillJobs();
        } else {
            NotificationManager.showWarning("Temperature NOT added!", NotificationManager.CENTER);
        }
    }

    public void onClick$btnOff() {
        dbxTemperature.setValue(GuiSettings.MIN_TEMP);
    }

    public void onClick$btnMore() {
        if (dbxTemperature.getValue() < GuiSettings.MAX_TEMP) {
            dbxTemperature.setValue(dbxTemperature.getValue() + 0.5);
        } else {
            NotificationManager.showWarning("has to be <= " + GuiSettings.MAX_TEMP, dbxTemperature, NotificationManager.AFTER_CENTER);
        }
    }

    public void onClick$btnLess() {
        if (dbxTemperature.getValue() > GuiSettings.MIN_TEMP) {
            dbxTemperature.setValue(dbxTemperature.getValue() - 0.5);
        } else {
            NotificationManager.showWarning("has to be >= " + GuiSettings.MIN_TEMP, dbxTemperature, NotificationManager.AFTER_CENTER);
        }
    }

    public void onClick$btnLogs() {
        WindowMaker.createWindow("log.zul", "Logs", null, true);
    }

    public void onClick$btnThermostats() {
        WindowMaker.createWindow("thermostat_listview.zul", "Thermostats", null, true);
    }
}
