package com.ziver.tab_layouts.internal.layout;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CtlEntryPlacementResolver {
    private CtlEntryPlacementResolver() {}

    public static List<CtlEntry> resolve(List<CtlEntry> entries, HolderLookup.Provider registries) {
        List<CtlEntry> first = entriesOf(entries, CtlEntryPlacementType.FIRST);
        List<CtlEntry> normal = entriesOf(entries, CtlEntryPlacementType.NORMAL);
        List<CtlEntry> last = entriesOf(entries, CtlEntryPlacementType.LAST);

        first.sort(ordering());
        normal.sort(ordering());
        last.sort(ordering());

        List<CtlEntry> result = new ArrayList<>(first);

        for (CtlEntry normalEntry : normal) {
            List<CtlEntry> before = targeted(entries, CtlEntryPlacementType.BEFORE, normalEntry, normal, registries);
            before.sort(ordering());
            result.addAll(before);

            result.add(normalEntry);

            List<CtlEntry> after = targeted(entries, CtlEntryPlacementType.AFTER, normalEntry, normal, registries);
            after.sort(ordering());
            result.addAll(after);
        }

        List<CtlEntry> unresolvedBefore = unresolved(entries, CtlEntryPlacementType.BEFORE, normal, registries);
        List<CtlEntry> unresolvedAfter = unresolved(entries, CtlEntryPlacementType.AFTER, normal, registries);

        unresolvedBefore.sort(ordering());
        unresolvedAfter.sort(ordering());

        result.addAll(unresolvedBefore);
        result.addAll(unresolvedAfter);
        result.addAll(last);

        return result;
    }

    private static List<CtlEntry> entriesOf(List<CtlEntry> entries, CtlEntryPlacementType type) {
        List<CtlEntry> result = new ArrayList<>();

        for (CtlEntry entry : entries) {
            if (entry.placementType() == type) {
                result.add(entry);
            }
        }

        return result;
    }

    private static List<CtlEntry> targeted(List<CtlEntry> entries, CtlEntryPlacementType type, CtlEntry anchor, List<CtlEntry> normalEntries, HolderLookup.Provider registries) {
        List<CtlEntry> result = new ArrayList<>();

        ItemLike anchorItem = itemLike(anchor, registries);
        if (anchorItem == null) return result;

        int anchorOccurrence = occurrenceOf(anchor, normalEntries, registries);
        if (anchorOccurrence <= 0) return result;

        for (CtlEntry entry : entries) {
            if (entry.placementType() != type) continue;
            if (entry.target() != anchorItem) continue;

            int clampedOccurrence = clampOccurrence(entry.target(), entry.targetOccurrence(), normalEntries, registries);

            if (clampedOccurrence == anchorOccurrence) {
                result.add(entry);
            }
        }

        return result;
    }

    private static List<CtlEntry> unresolved(List<CtlEntry> entries, CtlEntryPlacementType type, List<CtlEntry> normalEntries, HolderLookup.Provider registries) {
        List<CtlEntry> result = new ArrayList<>();

        for (CtlEntry entry : entries) {
            if (entry.placementType() != type) continue;
            if (countOccurrences(entry.target(), normalEntries, registries) <= 0) {
                result.add(entry);
            }
        }

        return result;
    }

    private static int occurrenceOf(CtlEntry entry, List<CtlEntry> normalEntries, HolderLookup.Provider registries) {
        ItemLike target = itemLike(entry, registries);
        if (target == null) return -1;

        int occurrence = 0;

        for (CtlEntry normalEntry : normalEntries) {
            ItemLike current = itemLike(normalEntry, registries);
            if (current == target) occurrence++;
            if (normalEntry == entry) return occurrence;
        }

        return -1;
    }

    private static int clampOccurrence(ItemLike target, int requestedOccurrence, List<CtlEntry> normalEntries, HolderLookup.Provider registries) {
        int count = countOccurrences(target, normalEntries, registries);
        if (count <= 0) return -1;
        return Math.min(requestedOccurrence, count);
    }

    private static int countOccurrences(ItemLike target, List<CtlEntry> normalEntries, HolderLookup.Provider registries) {
        if (target == null) return 0;

        int count = 0;

        for (CtlEntry entry : normalEntries) {
            ItemLike current = itemLike(entry, registries);
            if (current == target) count++;
        }

        return count;
    }

    private static ItemLike itemLike(CtlEntry entry, HolderLookup.Provider registries) {
        ItemStack stack = entry.buildStack(registries);
        return stack.isEmpty() ? null : stack.getItem();
    }

    private static Comparator<CtlEntry> ordering() {
        return Comparator.comparingLong(CtlEntry::priority).thenComparingLong(CtlEntry::insertionOrder);
    }
}