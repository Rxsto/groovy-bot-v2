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

package co.groovybot.bot.commands.music;

import co.groovybot.bot.core.audio.MusicPlayer;
import co.groovybot.bot.core.audio.Scheduler;
import co.groovybot.bot.core.command.CommandCategory;
import co.groovybot.bot.core.command.CommandEvent;
import co.groovybot.bot.core.command.Result;
import co.groovybot.bot.core.command.permission.Permissions;
import co.groovybot.bot.core.command.voice.SameChannelCommand;
import co.groovybot.bot.core.entity.EntityProvider;

public class LoopCommand extends SameChannelCommand {

    public LoopCommand() {
        super(new String[]{"loop", "lp"}, CommandCategory.MUSIC, Permissions.djMode(), "Lets you toggle through all loop-modes", "");
    }

    @Override
    public Result runCommand(String[] args, CommandEvent event, MusicPlayer player) {
        Scheduler scheduler = player.getScheduler();

        if (!scheduler.isLoop() && !scheduler.isLoopqueue()) {
            scheduler.setLoop(true);
            return send(noTitle("\uD83D\uDD02 " + event.translate("command.loop.song")));
        } else if (scheduler.isLoop()) {
            if (!Permissions.tierOne().isCovered(EntityProvider.getUser(event.getAuthor().getIdLong()).getPermissions(), event)) {
                scheduler.setLoop(false);
                send(noTitle("ℹ " + event.translate("command.loop.none")));
                return send(info(event.translate("command.loop.premium.title"), event.translate("command.loop.premium.description")));
            } else {
                scheduler.setLoop(false);
                scheduler.setLoopqueue(true);
                return send(noTitle("\uD83D\uDD01 " + event.translate("command.loop.queue")));
            }
        } else if (scheduler.isLoopqueue()) {
            scheduler.setLoopqueue(false);
            return send(noTitle("ℹ " + event.translate("command.loop.none")));
        } else {
            return send(error(event));
        }
    }
}
