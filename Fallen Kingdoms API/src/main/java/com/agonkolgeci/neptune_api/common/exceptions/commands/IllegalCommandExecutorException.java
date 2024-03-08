package com.agonkolgeci.neptune_api.common.exceptions.commands;

public class IllegalCommandExecutorException extends IllegalStateException {

    public IllegalCommandExecutorException() {
        super("Uniquement les joueurs peuvent exécuter cette commande.");
    }

}
