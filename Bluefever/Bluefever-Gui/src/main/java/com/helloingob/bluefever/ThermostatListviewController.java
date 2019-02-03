package com.helloingob.bluefever;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.helloingob.bluefever.data.ThermostatAssetDAO;
import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.to.ThermostatAssetTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.NotificationManager;
import com.helloingob.bluefever.util.WindowMaker;

public class ThermostatListviewController extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = -5633010826677724966L;
    private Listbox lbxThermostats;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        loadThermostats(ThermostatDAO.get());
    }

    public void onClick$btnAddThermostat() {
        WindowMaker.createWindow("thermostat_form.zul", "Add thermostat", null, false).addEventListener("onListChanged", new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                loadThermostats(ThermostatDAO.get());
            }
        });
    }

    public void onClick$btnEditThermostat() {
        if (lbxThermostats.getSelectedItem() != null) {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("thermostat", lbxThermostats.getSelectedItem().getValue());
            WindowMaker.createWindow("thermostat_form.zul", "Edit thermostat", parameter, false).addEventListener("onListChanged", new EventListener<Event>() {
                public void onEvent(Event event) throws Exception {
                    loadThermostats(ThermostatDAO.get());
                }
            });
        } else {
            NotificationManager.showWarning("Select thermostat!", lbxThermostats, NotificationManager.RIGHT);
        }
    }

    public void onClick$btnDeleteThermostat() {
        if (lbxThermostats.getSelectedItem() != null) {
            ThermostatTO thermostat = lbxThermostats.getSelectedItem().getValue();
            Messagebox.show(String.format("Are you sure to delete '%s'?", thermostat.getName()), "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {
                public void onEvent(Event event) throws InterruptedException {
                    if (event.getName().equals(Events.ON_OK)) {
                        ThermostatTO thermostat = (ThermostatTO) lbxThermostats.getSelectedItem().getValue();
                        ThermostatDAO.delete(thermostat);
                        loadThermostats(ThermostatDAO.get());
                    }
                }
            });
        } else {
            NotificationManager.showWarning("Select thermostat!", lbxThermostats, NotificationManager.RIGHT);
        }
    }

    private void loadThermostats(List<ThermostatTO> thermostats) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        lbxThermostats.getItems().clear();

        for (ThermostatTO thermostat : thermostats) {
            Listitem listitem = new Listitem();
            listitem.setValue(thermostat);

            Listcell listcellCurrent = new Listcell();
            Label labelCurrent = new Label("?");
            ThermostatAssetTO thermostatAsset = ThermostatAssetDAO.getLastAssets(thermostat.getPk());
            if (thermostatAsset != null && thermostatAsset.getDate() != null) {
                String lastPoll = dateTimeFormatter.format(thermostatAsset.getDate().toLocalDateTime());
                if (lastPoll == null) {
                    lastPoll = "?";
                }
                labelCurrent = new Label(lastPoll);
            }
            listcellCurrent.appendChild(labelCurrent);
            listitem.appendChild(listcellCurrent);

            Listcell listcellName = new Listcell();
            Label labelName = new Label(thermostat.getName());
            listcellName.appendChild(labelName);
            listitem.appendChild(listcellName);

            Listcell listcellAddress = new Listcell();
            Label labelAddress = new Label(thermostat.getAddress());
            listcellAddress.appendChild(labelAddress);
            listitem.appendChild(listcellAddress);

            Listcell listcellFirmware = new Listcell();
            Label labelFirmware = new Label(thermostat.getFirmware());
            listcellFirmware.appendChild(labelFirmware);
            listitem.appendChild(listcellFirmware);

            Listcell listcellSoftware = new Listcell();
            Label labelSoftware = new Label(thermostat.getSoftware());
            listcellSoftware.appendChild(labelSoftware);
            listitem.appendChild(listcellSoftware);

            Listcell listcellManufacturer = new Listcell();
            Label labelManufacturer = new Label(thermostat.getManufacturer());
            listcellManufacturer.appendChild(labelManufacturer);
            listitem.appendChild(listcellManufacturer);

            Listcell listcellDevicename = new Listcell();
            Label labelDevicename = new Label(thermostat.getDevicename());
            listcellDevicename.appendChild(labelDevicename);
            listitem.appendChild(listcellDevicename);

            lbxThermostats.appendChild(listitem);
        }
    }

}
