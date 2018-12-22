/*
 * Groovy Bot - The core component of the Groovy Discord music bot
 *
 * Copyright (C) 2018  Oskar Lang & Michael Rittmeister & Sergeij Herdt & Yannick Seeger & Justus Kliem & Leon Kappes
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package co.groovybot.bot.commands.general;

import co.groovybot.bot.core.KeyManager;
import co.groovybot.bot.core.command.*;
import co.groovybot.bot.core.command.permission.Permissions;
import co.groovybot.bot.core.entity.Key;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class KeyCommand extends Command {

    public KeyCommand() {
        super(new String[]{"key", "redeem"}, CommandCategory.GENERAL, Permissions.everyone(), "Allows you to redeem keys", "");
        registerSubCommand(new CreateCommand());
    }

    @Override
    public Result run(String[] args, CommandEvent event) {
        if (args.length == 0)
            return sendHelp();

        KeyManager keyManager = event.getBot().getKeyManager();

        if (!keyManager.keyExists(args[0]))
            return send(error(event.translate("command.key.invalidkey.title"), event.translate("command.key.invalidkey.description")));

        Key key = keyManager.getKey(args[0]);

        try {
            key.redeem(event.getAuthor());
        } catch (Exception e) {
            log.error("[Key] Error occurred while redeeming key", e);
            return send(error(event));
        }

        return send(success(event.translate("command.key.redeemed.title"), String.format(event.translate("command.key.redeemed.description"), key.getType().getDisplayName())));
    }

    private class CreateCommand extends SubCommand {

        public CreateCommand() {
            super(new String[]{"create", "generate"}, Permissions.ownerOnly(), "Allows you to create keys", "<type>");
        }

        @Override
        public Result run(String[] args, CommandEvent event) {
            if (args.length < 1)
                return sendHelp();

            Key.KeyType type;

            try {
                type = Key.KeyType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                return send(error(event.translate("command.key.invalidargument.title"), event.translate("command.key.invalidargument.title")));
            }

            UUID id = event.getBot().getKeyManager().generateKey(type);

            return send(success(event.translate("command.key.created.title"), String.format(event.translate("command.key.created.description"), id.toString())));
        }
    }
}
