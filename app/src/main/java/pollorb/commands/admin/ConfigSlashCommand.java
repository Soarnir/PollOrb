package pollorb.commands.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pollorb.commands.AbstractSlashCommand;
import pollorb.commands.CommandLevel;
import pollorb.commands.ContextualRequirements;
import pollorb.database.configs.GuildConfig;
import pollorb.database.tablehandlers.GuildConfigHandler;

import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Soarnir
 * @since 0.1.0
 */
public class ConfigSlashCommand extends AbstractSlashCommand {

    private static MessageEmbed botConfigEmbed;
    private static MessageEmbed developmentConfigEmbed;
    private static MessageEmbed adminConfigEmbed;
    private static MessageEmbed funConfigEmbed;
    private static final Logger logger = LoggerFactory.getLogger(ConfigSlashCommand.class);

    public ConfigSlashCommand() {
        super("config", "Configure things", CommandLevel.ADMINISTRATIVE, List.of(ContextualRequirements.ROLE));
        this.slashCommandOptionList = List.of(
            new OptionData(OptionType.STRING, "set", "The config to configure", false),
            new OptionData(OptionType.STRING, "value", "The value of the config", false)
        );
    }

    @Override
    public void handleSlashCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        GuildConfig guildConfig = GuildConfigHandler.getGuildConfig(guild.getIdLong());
        String setOption = event.getOption("set") == null ? "" : event.getOption("set", OptionMapping::getAsString);
        String valueOption = event.getOption("value") == null ? "" :event.getOption("value", OptionMapping::getAsString);

        if (!setOption.isEmpty() && !valueOption.isEmpty()) {
            // Both options provided
            switch (setOption.toLowerCase()) {
                case "logchannel", "logc":
                    TextChannel textChannel = guild.getTextChannelById(valueOption.substring(2, valueOption.length() - 1));
                    if (textChannel == null) {
                        errorEmbed(event, "Could not find channel");
                    } else if (!textChannel.canTalk()) {
                        errorEmbed(event, "I do not have permission to post in that channel");
                    } else {
                        String oldChannelMention = "Not Set";
                        long oldChannel = guildConfig.getLoggingChannel();
                        if (oldChannel != 0L) oldChannelMention = guild.getChannelById(TextChannel.class, oldChannel).getAsMention();
                        guildConfig.setLoggingChannel(textChannel.getIdLong());
                        GuildConfigHandler.update(guild.getIdLong(), guildConfig);
                        sendConfigMessage(event, "Log channel", oldChannelMention, textChannel.getAsMention());
                    }
                    return;
                case "managementrole", "mrole":
                    Role role = guild.getRoleById(valueOption.substring(3, valueOption.length() - 1));
                    if (role == null) {
                        errorEmbed(event, "Could not find a valid role");
                    } else {
                        String oldRoleMention = "Not set";
                        long oldRole = guildConfig.getGuildBotManager();
                        if (oldRole != 0L) oldRoleMention = guild.getRoleById(oldRole).getAsMention();
                        guildConfig.setGuildBotManager(role.getIdLong());
                        GuildConfigHandler.update(guild.getIdLong(), guildConfig);
                        sendConfigMessage(event, "Management role", oldRoleMention, role.getAsMention());
                    }
                case "prefix":
                    String oldPrefix = guildConfig.getPrefix();
                    guildConfig.setPrefix(valueOption);
                    GuildConfigHandler.update(guild.getIdLong(), guildConfig);
                    sendConfigMessage(event, "Prefix", oldPrefix, valueOption);
            }
        } else if (!setOption.isEmpty()) {
            // Only set provided
            errorEmbed(event, "You need to provide a value for this config as well.");
        } else if (!valueOption.isEmpty()) {
            // Only value provided
            errorEmbed(event, "You need to specify which config it is you wish to configure.");
        } else {
            // No options provided, show full config menu
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(new Color(123,123,123))
                .setTitle("Config")
                .setDescription("*Configuration for " + event.getGuild().getName() + "*")
                .setFooter("Footer text");

            // Build config embeds
            botConfigEmbed = botConfigEmbed(event, guild, guildConfig, embedBuilder);
            developmentConfigEmbed = developmentConfigEmbed(event, guild, guildConfig, embedBuilder);
            adminConfigEmbed = adminConfigEmbed(event, guild, guildConfig, embedBuilder);
            funConfigEmbed = funConfigEmbed(event, guild, guildConfig, embedBuilder);

            // Left and right
            // Button.of(ButtonStyle.SUCCESS, "left", "Left").withEmoji(Emoji.fromUnicode("U+2B05 U+FE0F")),
            // Button.of(ButtonStyle.SUCCESS, "right", "right").withEmoji(Emoji.fromUnicode("U+27A1 U+FE0F"))

            event.replyEmbeds(adminConfigEmbed)
                .setActionRow(
                    Button.of(ButtonStyle.PRIMARY, "config.bot", "Bot").withEmoji(Emoji.fromUnicode("U+1F9F0")),
                    Button.of(ButtonStyle.PRIMARY, "config.development", "Development").withEmoji(Emoji.fromUnicode("U+1F6E0 U+FE0F")),
                    Button.of(ButtonStyle.PRIMARY, "config.admin", "Admin").withEmoji(Emoji.fromUnicode("U+1F9D1 U+200D U+1F4BB")),
                    Button.of(ButtonStyle.PRIMARY, "config.fun", "Fun").withEmoji(Emoji.fromUnicode("U+37 U+FE0F U+20E3"))
                )
                .setEphemeral(true)
                .queue();
        }
    }

    @Override
    public void handleButtonInteraction(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        switch (Objects.requireNonNull(event.getButton().getId())) {
            case "config.bot":
                event.getHook().editMessageEmbedsById(event.getMessageId(), botConfigEmbed).queue();
                return;
            case "config.development":
                event.getHook().editMessageEmbedsById(event.getMessageId(), developmentConfigEmbed).queue();
                return;
            case "config.admin":
                event.getHook().editMessageEmbedsById(event.getMessageId(), adminConfigEmbed).queue();
                return;
            case "config.fun":
                event.getHook().editMessageEmbedsById(event.getMessageId(), funConfigEmbed).queue();
        }
    }

    // may convert these to make use of categories in order to generate embeds, multiple separate methods may
    // not be worthwhile to maintain
    public MessageEmbed botConfigEmbed(SlashCommandInteractionEvent event, Guild guild, GuildConfig guildConfig, EmbedBuilder embedBuilder) {
        EmbedBuilder embedBuilderInt = new EmbedBuilder(embedBuilder);
        embedBuilderInt.setColor(new Color(0, 0, 0))
            .setTitle("Bot Config")
            .setDescription("*Configuration for PollOrb*");

        return embedBuilderInt.build();
    }

    public MessageEmbed developmentConfigEmbed(SlashCommandInteractionEvent event, Guild guild, GuildConfig guildConfig, EmbedBuilder embedBuilder) {
        EmbedBuilder embedBuilderInt = new EmbedBuilder(embedBuilder);

        return embedBuilderInt.build();
    }

    public MessageEmbed adminConfigEmbed(SlashCommandInteractionEvent event, Guild guild, GuildConfig guildConfig, EmbedBuilder embedBuilder) {
        EmbedBuilder embedBuilderInt = new EmbedBuilder(embedBuilder);

        // Bot management
        String role = guildConfig.getGuildBotManager() == 0L ? "Not set" : guild.getRoleById(guildConfig.getGuildBotManager()).getAsMention();
        embedBuilderInt.addField("Management role",
            role + "\n*Config aliases:* `managementrole, mrole`" +
                "\n*Note that admins can always configure the bot, this config allows users with this role administrative permissions over the bot, such as moderators.*",
            false);

        // Logging channel
        String channel = guildConfig.getLoggingChannel() == 0L ? "Not set" : (guild.getGuildChannelById(guildConfig.getLoggingChannel()).getAsMention());
        embedBuilderInt.addField("Logging channel",
            channel + "\n*Config aliases:* `logchannel, logc`",
            false);

        // Prefix
        embedBuilderInt.addField("Prefix",
            "`" + guildConfig.getPrefix() + "`" +
                "\n*Config aliases:* `prefix`",
            false);

        return embedBuilderInt.build();
    }

    public MessageEmbed funConfigEmbed(SlashCommandInteractionEvent event, Guild guild, GuildConfig guildConfig, EmbedBuilder embedBuilder) {
        EmbedBuilder embedBuilderInt = new EmbedBuilder(embedBuilder);

        return embedBuilderInt.build();
    }

    public void sendConfigMessage(SlashCommandInteractionEvent event, String config, String oldConfigValue, String newConfigValue) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(new Color(123,123,123))
            .setTitle("Config updated")
            .setDescription("*" + config + " for " + event.getGuild().getName() + " updated*")
            .addField("From", oldConfigValue, true)
            .addField("To", newConfigValue, true)
            .setFooter("Footer text");
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
