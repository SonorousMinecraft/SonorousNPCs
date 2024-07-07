package com.sereneoasis.utils;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Pair;
import com.sereneoasis.SereneNPCs;
import com.sereneoasis.entity.SereneHumanEntity;
import com.sereneoasis.npc.random.types.BasicNPC;
import com.sereneoasis.npc.random.types.assassin.AssassinEntity;
import com.sereneoasis.npc.random.types.baker.BakerEntity;
import com.sereneoasis.npc.random.types.butcher.ButcherEntity;
import com.sereneoasis.npc.random.types.mage.MageEntity;
import com.sereneoasis.npc.random.types.rogue.RogueEntity;
import com.sereneoasis.npc.random.types.warrior.WarriorEntity;
import com.sereneoasis.npc.storyline.Guide;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.javatuples.Triplet;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class NPCUtils {

    private static final Stack<Triplet<String, String, String>> NAME_VALUE_SIGNATURE = new Stack<>();

    public static void spawnGuideNPC(Location location, Player player, String name, String skinUsersIGN) {

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        Guide serverPlayer = new Guide(minecraftServer, serverLevel, setSkin(gameProfile), ClientInformation.createDefault());

        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData synchedEntityData = serverPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        Connection serverPlayerConnection = new Connection(PacketFlow.SERVERBOUND);

        serverPlayerConnection.channel = ((CraftPlayer) player).getHandle().connection.connection.channel;

        CommonListenerCookie commonListenerCookie = CommonListenerCookie.createInitial(gameProfile);
        ServerGamePacketListenerImpl serverGamePacketListener = new ServerGamePacketListenerImpl(minecraftServer, serverPlayerConnection, serverPlayer, commonListenerCookie);
        serverPlayer.connection = serverGamePacketListener;


        ClientboundPlayerInfoUpdatePacketWrapper playerInfoPacket = new ClientboundPlayerInfoUpdatePacketWrapper(
                EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY),
                serverPlayer,
                180,
                true
        );
        PacketUtils.sendPacket(playerInfoPacket.getPacket(), player);


        serverLevel.addFreshEntity(serverPlayer);
    }

    public static BasicNPC spawnNPC(Location location, Player player, String name) {
        //ServerPlayer player = ((CraftPlayer)p).getHandle();

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        Random random = new Random();
        BasicNPC serverPlayer = null;
        GameProfile skinGameProfile = setSkin(gameProfile);
        switch (random.nextInt(6)) {
            case 0 -> {
                serverPlayer = new AssassinEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
            case 1 -> {
                serverPlayer = new BakerEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
            case 2 -> {
                serverPlayer = new ButcherEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
            case 3 -> {
                serverPlayer = new MageEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
            case 4 -> {
                serverPlayer = new RogueEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
            case 5 -> {
                serverPlayer = new WarriorEntity(minecraftServer, serverLevel, skinGameProfile, ClientInformation.createDefault());

            }
        }
        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData synchedEntityData = serverPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        Connection serverPlayerConnection = new Connection(PacketFlow.SERVERBOUND);

        serverPlayerConnection.channel = ((CraftPlayer) player).getHandle().connection.connection.channel;

        CommonListenerCookie commonListenerCookie = CommonListenerCookie.createInitial(gameProfile);
        ServerGamePacketListenerImpl serverGamePacketListener = new ServerGamePacketListenerImpl(minecraftServer, serverPlayerConnection, serverPlayer, commonListenerCookie);
        serverPlayer.connection = serverGamePacketListener;

        addNPC(player, serverPlayer);


        serverLevel.addFreshEntity(serverPlayer);
        SereneNPCs.plugin.addNPC(serverPlayer);

        return serverPlayer;
    }

    public static void updateEquipment(SereneHumanEntity npc, Player player) {
        List<Pair<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            equipment.add(new Pair<EquipmentSlot, ItemStack>(slot, npc.getItemBySlot(slot)));
        }
        ClientboundSetEquipmentPacket clientboundSetEquipmentPacket =
                new ClientboundSetEquipmentPacket(npc.getId(), equipment);
        PacketUtils.sendPacket(clientboundSetEquipmentPacket, player);
    }

    public static String getJson(String url) {
        String json = getStringFromURL(url);
        return json;
    }

    public static GameProfile setSkin(GameProfile gameProfile) {

        Triplet<String, String, String> triplet = NAME_VALUE_SIGNATURE.peek();
        if (NAME_VALUE_SIGNATURE.size() > 1) {
            NAME_VALUE_SIGNATURE.pop();
        }
        String name = triplet.getValue0();
        String value = triplet.getValue1();
        String signature = triplet.getValue2();

        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put("name", new Property("name", name));
        propertyMap.put("textures", new Property("textures", value, signature));
        return gameProfile;
    }

    public static void changeSkin(String value, String signature, GameProfile gameProfile) {
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
    }

    public static void initUUID(int counter, JavaPlugin plugin) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            Faker faker = new Faker();
            String newUrl = "https://api.mojang.com/users/profiles/minecraft/" + faker.name().firstName();
            String json = getJson(newUrl);
            if (!json.isEmpty()) {
                Gson gson = new Gson();
                String uuid = gson.fromJson(json, JsonObject.class).get("id").getAsString();
                initProperties(faker.name().firstName(), uuid);
            }
            if (counter < 1000) {
                initUUID(counter + 1, plugin);
            }

        }, 2L);
    }

    private static void initProperties(String name, String uuid) {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
        String json = getStringFromURL(url);
        Gson gson = new Gson();
        JsonObject mainObject = gson.fromJson(json, JsonObject.class);
        JsonObject jsonObject = mainObject.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        String value = jsonObject.get("value").getAsString();
        String signature = jsonObject.get("signature").getAsString();
        Triplet<String, String, String> triplet = new Triplet<>(name, value, signature);
        NAME_VALUE_SIGNATURE.add(triplet);

    }


    private static String getStringFromURL(String url) {
        StringBuilder text = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                while (line.startsWith(" ")) {
                    line = line.substring(1);
                }
                text.append(line);
            }
            scanner.close();
        } catch (IOException exception) {
//            exception.printStackTrace();
        }
        return text.toString();
    }

    public static void addNPC(Player player, BasicNPC serverPlayer) {
        ClientboundPlayerInfoUpdatePacketWrapper playerInfoPacket = new ClientboundPlayerInfoUpdatePacketWrapper(
                EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY),
                serverPlayer,
                180,
                true
        );
        PacketUtils.sendPacket(playerInfoPacket.getPacket(), player);
    }

    public static void toggleOff(BasicNPC basicNpc) {
        basicNpc.toggleOff();
    }

    public static void toggleOn(BasicNPC basicNpc) {
        basicNpc.toggleOn();
    }
}