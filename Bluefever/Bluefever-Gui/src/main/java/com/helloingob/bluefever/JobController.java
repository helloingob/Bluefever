package com.helloingob.bluefever;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Timebox;

import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.ThermostatJobDAO;
import com.helloingob.bluefever.data.to.ThermostatJobTO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.GuiSettings;
import com.helloingob.bluefever.util.NotificationManager;

public class JobController extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = 5216883436447833284L;
    private Hbox hbxDate;
    private Hbox hbxTime;
    private Hbox hbxDayOfWeek;

    private Combobox cbxThermostat;
    private Combobox cbxDayOfWeek;
    private Datebox dbxDate;
    private Timebox tbxTime;
    private Doublebox dbxTemperature;
    private Radio rdoRegular;
    private Radio rdoSpecific;
    private Checkbox cbxActive;

    private ThermostatJobTO thermostatJob = null;
    private List<ThermostatTO> thermostats;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        fillDayOfWeek();
        thermostats = ThermostatDAO.get();
        fillThermostats();
        cbxDayOfWeek.setSelectedIndex(0);
        Date date = new Date();
        tbxTime.setValue(date);
        dbxDate.setValue(date);
        dbxTemperature.setValue(20);
        cbxDayOfWeek.setSelectedIndex(LocalDate.now().getDayOfWeek().getValue() - 1);

        if (arg.containsKey("job")) {
            thermostatJob = (ThermostatJobTO) arg.get("job");
            prefillForm();
        }
    }

    private void fillDayOfWeek() {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setLabel(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN));
            comboitem.setValue(dayOfWeek.getValue());
            cbxDayOfWeek.appendChild(comboitem);
        }
    }

    public void onClick$btnOk() {
        if (allFieldsFilled()) {
            ThermostatJobTO newThermostatJob = new ThermostatJobTO();
            newThermostatJob.setActive(cbxActive.isChecked());
            newThermostatJob.setTemperature(dbxTemperature.getValue());
            newThermostatJob.setThermostatPk((Integer) cbxThermostat.getSelectedItem().getValue());

            if (rdoRegular.isChecked()) {
                newThermostatJob.setDayOfWeek(Integer.parseInt(cbxDayOfWeek.getSelectedItem().getValue().toString()));
                newThermostatJob.setTime(new Time(tbxTime.getValue().getTime()));
            } else {
                newThermostatJob.setDate(new Timestamp(dbxDate.getValue().getTime()));
            }

            if (thermostatJob != null) {
                newThermostatJob.setPk(thermostatJob.getPk());

                if (ThermostatJobDAO.update(newThermostatJob)) {
                    NotificationManager.showInfo("Job successfully edited.", NotificationManager.CENTER);
                    Events.postEvent("onListChanged", this.self, null);
                    this.self.detach();
                } else {
                    NotificationManager.showWarning("Job NOT edited!", NotificationManager.CENTER);
                }
            } else {
                //add
                if (ThermostatJobDAO.add(newThermostatJob)) {
                    NotificationManager.showInfo("Job successfully added.", NotificationManager.CENTER);
                    Events.postEvent("onListChanged", this.self, null);
                    this.self.detach();
                } else {
                    NotificationManager.showWarning("Job NOT added!", NotificationManager.CENTER);
                }
            }

        }
    }

    private void prefillForm() {
        cbxActive.setChecked(thermostatJob.isActive());

        for (ThermostatTO thermostat : thermostats) {
            if (thermostat.getPk().equals(thermostatJob.getThermostatPk())) {
                cbxThermostat.setValue(thermostat.getName());
                break;
            }
        }
        cbxThermostat.select();
        dbxTemperature.setValue(thermostatJob.getTemperature());

        //regular
        if (thermostatJob.getDayOfWeek() != null) {
            onClick$rdoRegular();
            rdoRegular.setChecked(true);
            cbxDayOfWeek.setSelectedIndex(thermostatJob.getDayOfWeek() - 1); //Monday == 0
            tbxTime.setValue(new Date(thermostatJob.getTime().getTime()));
        } else {
            //specific
            rdoSpecific.setChecked(true);
            onClick$rdoSpecific();
            dbxDate.setValue(thermostatJob.getDate());
        }
    }

    private void fillThermostats() {
        for (ThermostatTO thermostat : ThermostatDAO.get()) {
            Comboitem comboitem = new Comboitem();
            comboitem.setValue(thermostat.getPk());
            comboitem.setLabel(thermostat.getName());
            cbxThermostat.appendChild(comboitem);
        }
        cbxThermostat.setSelectedIndex(0);
    }

    public void onClick$rdoRegular() {
        hbxDate.setVisible(false);
        hbxTime.setVisible(true);
        hbxDayOfWeek.setVisible(true);
    }

    public void onClick$rdoSpecific() {
        hbxTime.setVisible(false);
        hbxDayOfWeek.setVisible(false);
        hbxDate.setVisible(true);
    }

    private boolean allFieldsFilled() {
        if (dbxTemperature.getValue() > GuiSettings.MAX_TEMP) {
            NotificationManager.showWarning("has to be <= " + GuiSettings.MAX_TEMP, dbxTemperature, NotificationManager.RIGHT);
            return false;
        }
        if (dbxTemperature.getValue() < GuiSettings.MIN_TEMP) {
            NotificationManager.showWarning("has to be >= " + GuiSettings.MIN_TEMP, dbxTemperature, NotificationManager.RIGHT);
            return false;
        }
        return true;
    }

    public void onClick$btnOff() {
        dbxTemperature.setValue(GuiSettings.MIN_TEMP);
    }

    public void onClick$btnMore() {
        if (dbxTemperature.getValue() < GuiSettings.MAX_TEMP) {
            dbxTemperature.setValue(dbxTemperature.getValue() + 0.5);
        } else {
            NotificationManager.showWarning("has to be <= " + GuiSettings.MAX_TEMP, dbxTemperature, NotificationManager.RIGHT);
        }
    }

    public void onClick$btnLess() {
        if (dbxTemperature.getValue() > GuiSettings.MIN_TEMP) {
            dbxTemperature.setValue(dbxTemperature.getValue() - 0.5);
        } else {
            NotificationManager.showWarning("has to be >= " + GuiSettings.MIN_TEMP, dbxTemperature, NotificationManager.RIGHT);
        }
    }

}
