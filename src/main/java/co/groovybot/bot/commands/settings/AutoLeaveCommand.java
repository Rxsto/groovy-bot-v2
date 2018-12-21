package co.groovybot.bot.commands.settings;

import co.groovybot.bot.core.command.Command;
import co.groovybot.bot.core.command.CommandCategory;
import co.groovybot.bot.core.command.CommandEvent;
import co.groovybot.bot.core.command.Result;
import co.groovybot.bot.core.command.permission.Permissions;
import net.dv8tion.jda.core.Permission;

public class AutoLeaveCommand extends Command {
    public AutoLeaveCommand() {
        super(new String[]{"autoleave", "al"}, CommandCategory.SETTINGS, Permissions.tierTwo(), "Lets you deactivate the auto-leave mode", "");
    }

    @Override
    public Result run(String[] args, CommandEvent event) {
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR))
            return send(error(event.translate("phrases.nopermission.title"), event.translate("phrases.nopermission.admin")));
        else {
            if (event.getGroovyGuild().isAutoLeave()) {
                event.getGroovyGuild().setAutoLeave(false);
                return send(success(event.translate("command.autoleave.disabled.title"), event.translate("command.autoleave.disabled.description")));
            } else {
                event.getGroovyGuild().setAutoLeave(true);
                return send(success(event.translate("command.autoleave.enabled.title"), event.translate("command.autoleave.enabled.description")));
            }
        }
    }
}
