package io.nightbeam.postmaster.api;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PostMasterAPIBridge {
    CompletableFuture<UUID> sendMail(UUID player, String sender, String message, ItemStack[] items, String voucherId);

    CompletableFuture<UUID> sendVoucher(UUID player, String sender, String voucherId);
}
