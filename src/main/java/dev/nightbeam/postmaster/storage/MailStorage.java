package dev.nightbeam.postmaster.storage;

import dev.nightbeam.postmaster.model.MailEntry;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MailStorage {
    StorageType getType();

    CompletableFuture<Void> init();

    CompletableFuture<Void> shutdown();

    CompletableFuture<List<MailEntry>> loadAll();

    CompletableFuture<Void> upsert(MailEntry mail);

    CompletableFuture<Void> upsertAll(Collection<MailEntry> mails);
}
