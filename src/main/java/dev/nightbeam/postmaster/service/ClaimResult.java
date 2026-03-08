package dev.nightbeam.postmaster.service;

public enum ClaimResult {
    SUCCESS,
    MAIL_NOT_FOUND,
    MAIL_EXPIRED,
    INVENTORY_FULL,
    VOUCHER_MISSING
}
