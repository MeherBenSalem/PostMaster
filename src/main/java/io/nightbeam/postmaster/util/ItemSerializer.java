package io.nightbeam.postmaster.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class ItemSerializer {
    private ItemSerializer() {
    }

    public static String serialize(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(item);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to serialize item", ex);
        }
    }

    public static ItemStack deserialize(String encoded) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(encoded));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            Object obj = dataInput.readObject();
            return (ItemStack) obj;
        } catch (IOException | ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize item", ex);
        }
    }

    public static String serializeList(List<ItemStack> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        List<String> out = new ArrayList<>();
        for (ItemStack item : items) {
            out.add(serialize(item));
        }
        return String.join(";", out);
    }

    public static List<ItemStack> deserializeList(String encodedList) {
        if (encodedList == null || encodedList.isBlank()) {
            return List.of();
        }
        String[] chunks = encodedList.split(";");
        List<ItemStack> items = new ArrayList<>(chunks.length);
        for (String chunk : chunks) {
            if (!chunk.isBlank()) {
                items.add(deserialize(chunk));
            }
        }
        return items;
    }
}
