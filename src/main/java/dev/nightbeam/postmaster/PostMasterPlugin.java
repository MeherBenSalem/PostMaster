package dev.nightbeam.postmaster;

import dev.nightbeam.postmaster.api.PostMasterAPI;
import dev.nightbeam.postmaster.api.PostMasterAPIBridge;
import dev.nightbeam.postmaster.command.MailCommand;
import dev.nightbeam.postmaster.config.PostMasterConfig;
import dev.nightbeam.postmaster.gui.GuiManager;
import dev.nightbeam.postmaster.listener.GuiListener;
import dev.nightbeam.postmaster.listener.PlayerJoinListener;
import dev.nightbeam.postmaster.scheduler.SchedulerAdapter;
import dev.nightbeam.postmaster.service.MailService;
import dev.nightbeam.postmaster.service.NotificationService;
import dev.nightbeam.postmaster.service.TargetResolver;
import dev.nightbeam.postmaster.service.VoucherService;
import dev.nightbeam.postmaster.storage.MailStorage;
import dev.nightbeam.postmaster.storage.StorageFactory;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PostMasterPlugin extends JavaPlugin implements PostMasterAPIBridge {
    private SchedulerAdapter scheduler;
    private PostMasterConfig postMasterConfig;
    private MailStorage storage;
    private VoucherService voucherService;
    private MailService mailService;
    private GuiManager guiManager;
    private TargetResolver targetResolver;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("vouchers/example-voucher.yml", false);

        this.scheduler = new SchedulerAdapter(this);
        this.postMasterConfig = PostMasterConfig.load(this);
        this.voucherService = new VoucherService(this);
        this.voucherService.reload();

        this.storage = StorageFactory.create(this, scheduler, postMasterConfig, postMasterConfig.storageType());
        this.storage.init().join();

        NotificationService notificationService = new NotificationService(postMasterConfig);
        this.mailService = new MailService(this, scheduler, storage, voucherService, postMasterConfig, notificationService);
        this.mailService.loadCache().join();

        this.guiManager = new GuiManager(scheduler, mailService, voucherService);
        this.targetResolver = new TargetResolver();

        MailCommand mailCommand = new MailCommand(this, guiManager, mailService, voucherService, targetResolver);
        registerCommand("mail", mailCommand);
        registerCommand("mailbox", mailCommand);

        getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(mailService), this);

        PostMasterAPI.registerBridge(this);
        getLogger().info("PostMaster enabled with storage=" + storage.getType() + " folia=" + scheduler.isFolia());
    }

    @Override
    public void onDisable() {
        try {
            if (mailService != null) {
                mailService.shutdown().join();
            }
            if (storage != null) {
                storage.shutdown().join();
            }
        } catch (Exception ex) {
            getLogger().warning("Error during shutdown: " + ex.getMessage());
        }
        PostMasterAPI.clearBridge();
    }

    public void reloadPluginRuntime() {
        this.postMasterConfig = PostMasterConfig.load(this);
        this.voucherService.reload();
    }

    @Override
    public CompletableFuture<UUID> sendMail(UUID player, String sender, String message, ItemStack[] items, String voucherId) {
        List<ItemStack> attachments = items == null ? List.of() : Arrays.stream(items).toList();
        return mailService.sendMail(player, sender, message, attachments, voucherId, List.of());
    }

    @Override
    public CompletableFuture<UUID> sendVoucher(UUID player, String sender, String voucherId) {
        return mailService.sendMail(
                player,
                sender,
                "&6Voucher Delivery\n\n&fA voucher has arrived: &e" + voucherId,
                List.of(),
                voucherId,
                List.of()
        );
    }

    private void registerCommand(String commandLabel, MailCommand executor) {
        PluginCommand command = getCommand(commandLabel);
        if (command == null) {
            throw new IllegalStateException("Command missing from plugin.yml: " + commandLabel);
        }
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
