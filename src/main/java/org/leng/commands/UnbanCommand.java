package org.leng.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.leng.LengbanList;
import org.leng.utils.Utils;

import java.util.Arrays;

public class UnbanCommand extends Command {
    public UnbanCommand() {
        super("unban");
        setAliases(Arrays.asList("pardon"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(sender instanceof Player){
            if (!sender.isOp()){
                return false;
            }
        }
        if (args.length == 0) {
            Utils.sendMessage(sender, "§c用法错误: 请提供要解除封禁的玩家名！");
            return false;
        }
        LengbanList.getInstance().banManager.unbanPlayer(args[0]);
        Utils.sendMessage(sender,"§l§a成功解除封禁 玩家: "+args[0]);
        return false;
    }
}
