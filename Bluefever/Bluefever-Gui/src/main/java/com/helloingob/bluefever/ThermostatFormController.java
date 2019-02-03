package com.helloingob.bluefever;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import com.helloingob.bluefever.data.ThermostatDAO;
import com.helloingob.bluefever.data.to.ThermostatTO;
import com.helloingob.bluefever.util.NotificationManager;

public class ThermostatFormController extends GenericForwardComposer<Component> {

    private static final long serialVersionUID = -6398172186465550678L;
    private Textbox tbxName;
    private Textbox tbxAddress;
    private Intbox ibxPin;

    private ThermostatTO thermostat = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();
    }

    private void init() {
        ibxPin.setValue(0);
        if (arg.containsKey("thermostat")) {
            thermostat = (ThermostatTO) arg.get("thermostat");
            prefillForm();
        }
    }

    private void prefillForm() {
        tbxName.setText(thermostat.getName());
        tbxAddress.setText(thermostat.getAddress());
    }

    public void onClick$btnOk() {
        if (allFieldsFilled()) {
            ThermostatTO newThermostat = new ThermostatTO();
            newThermostat.setName(tbxName.getValue());
            newThermostat.setAddress(tbxAddress.getValue());
            newThermostat.setPin(ibxPin.getValue());
            if (!ThermostatDAO.deviceExists(newThermostat)) {
                //edit
                if (thermostat != null) {
                    newThermostat.setPk(thermostat.getPk());

                    if (ThermostatDAO.update(newThermostat)) {
                        NotificationManager.showInfo("Thermostat successfully edited.", NotificationManager.CENTER);
                        Events.postEvent("onListChanged", this.self, null);
                        this.self.detach();
                    } else {
                        NotificationManager.showWarning("Thermostat NOT edited!", NotificationManager.CENTER);
                    }
                } else {
                    //add
                    if (ThermostatDAO.add(newThermostat)) {
                        NotificationManager.showInfo("Thermostat successfully added.", NotificationManager.CENTER);
                        Events.postEvent("onListChanged", this.self, null);
                        this.self.detach();
                    } else {
                        NotificationManager.showWarning("Thermostat NOT added!", NotificationManager.CENTER);
                    }
                }
            } else {
                NotificationManager.showWarning("Thermostat already exists!", NotificationManager.CENTER);
            }
        }
    }

    private boolean allFieldsFilled() {
        if (tbxName.getValue().isEmpty()) {
            NotificationManager.showWarning("Can't be empty!", tbxName, NotificationManager.RIGHT);
            return false;
        }
        if (tbxAddress.getValue().isEmpty()) {
            NotificationManager.showWarning("Can't be empty!", tbxAddress, NotificationManager.RIGHT);
            return false;
        }
        if (ibxPin.getValue() < 0) {
            NotificationManager.showWarning("Has to be >=0", ibxPin, NotificationManager.RIGHT);
            return false;
        }
        return true;
    }

}
