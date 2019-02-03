package com.helloingob.bluefever.command.exec;

import java.util.ArrayList;
import java.util.List;

public class CommandMerger {

    public enum Handle {
        //@formatter:off
        AUTHENTICATE ("0x0047"),
        FIRMWARE ("0x0016"),
        SOFTWARE ("0x0018"),
        MANUFACTURER ("0x001a"),
        DEVICENAME ("0x0003"),
        TEMPERATURE ("0x003f"),
        DATE ("0x001d");
        //@formatter:on

        private final String handle;

        private Handle(final String handle) {
            this.handle = handle;
        }

        @Override
        public String toString() {
            return handle;
        }
    }

    private String address;
    private List<String> commands;

    public CommandMerger(String address) {
        this.address = address;
        commands = new ArrayList<String>();
    }

    public void authenticate(String pin) {
        addWriteCommand(Handle.AUTHENTICATE, pin);
    }

    public void addWriteCommand(Handle handle, String value) {
        commands.add("sudo gatttool -b " + this.address + " --char-write-req -a " + handle + " -n " + value);
    }

    public void addReadCommand(Handle handle) {
        commands.add("sudo gatttool -b " + this.address + " --char-read -a " + handle);
    }

    public String getCommandline() {
        final String SLEEP = "; sleep 1; ";
        String commandLine = "";
        for (String command : commands) {
            commandLine += command + SLEEP;
        }

        if (commandLine.length() > 10) {
            commandLine = commandLine.substring(0, commandLine.length() - SLEEP.length());
        }
        return commandLine;
    }

}
