package com.sereneoasis.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Pair;
import com.sereneoasis.entity.HumanEntity;
import com.sereneoasis.npc.random.types.NPCMaster;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NPCUtils {

    public static void spawnGuideNPC(Location location, Player player, String name, String skinUsersIGN) {
//        ServerPlayer player = ((CraftPlayer)p).getHandle();

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        Guide serverPlayer = new Guide(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData synchedEntityData = serverPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        //PacketUtils.setValue(serverPlayer, "c", ((CraftPlayer) player).getHandle().connection);

//        serverPlayer.connection = ((CraftPlayer) player).getHandle().connection;
        Connection serverPlayerConnection = new Connection(PacketFlow.SERVERBOUND);

//        serverPlayerConnection.setListener(new PacketListenerImpl());
//        serverPlayerConnection.setListener(((CraftPlayer) player).getHandle().connection.connection.getPacketListener());
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


//        PacketUtils.sendPacket(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer), player);

//        SereneNPCs.getPacketListener().injectPlayer(serverPlayer.getBukkitEntity());

        serverLevel.addFreshEntity(serverPlayer);
    }

        public static NPCMaster spawnNPC(Location location, Player player, String name, String skinUsersIGN){
        //ServerPlayer player = ((CraftPlayer)p).getHandle();

        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);

        net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer)player).getHandle();
//        HumanEntity serverPlayer = new HumanEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());
        Random random = new Random();
        NPCMaster serverPlayer = null;
        switch (random.nextInt(6)) {
//        switch (0) {
            case 0 -> {
                serverPlayer = new AssassinEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
            case 1 -> {
                serverPlayer = new BakerEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
            case 2 -> {
                serverPlayer = new ButcherEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
            case 3 -> {
                serverPlayer = new MageEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
            case 4 -> {
                serverPlayer = new RogueEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
            case 5 -> {
                serverPlayer = new WarriorEntity(minecraftServer, serverLevel, setSkin(skinUsersIGN, gameProfile), ClientInformation.createDefault());

            }
        }
        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData synchedEntityData = serverPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        //PacketUtils.setValue(serverPlayer, "c", ((CraftPlayer) player).getHandle().connection);

//        serverPlayer.connection = ((CraftPlayer) player).getHandle().connection;
        Connection serverPlayerConnection = new Connection(PacketFlow.SERVERBOUND);

//        serverPlayerConnection.setListener(new PacketListenerImpl());
//        serverPlayerConnection.setListener(((CraftPlayer) player).getHandle().connection.connection.getPacketListener());
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


//        PacketUtils.sendPacket(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer), player);

//        SereneNPCs.getPacketListener().injectPlayer(serverPlayer.getBukkitEntity());

        serverLevel.addFreshEntity(serverPlayer);

//        serverPlayer.setLoadViewDistance(-1);
//        serverPlayer.setSendViewDistance(-1);
//        serverPlayer.setTickViewDistance(-1);
        //  serverPlayer.connection = null;


//        PacketUtils.sendPacket(new ClientboundAddEntityPacket(serverPlayer), player);
//        PacketUtils.sendPacket(new ClientboundSetEntityDataPacket(serverPlayer.getId(), synchedEntityData.getNonDefaultValues()), player);




//        Bukkit.getScheduler().runTaskLaterAsynchronously(SerenityEntities.getInstance(), new Runnable() {
//            @Override
//            public void run() {
//                PacketUtils.sendPacket(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID())), player);
//            }
//        }, 40);

        return serverPlayer;
    }


    public static void updateEquipment(HumanEntity npc, Player player){
        List<Pair<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            equipment.add(new Pair<EquipmentSlot, ItemStack> (slot, npc.getItemBySlot(slot)));
        }
        ClientboundSetEquipmentPacket clientboundSetEquipmentPacket =
                new ClientboundSetEquipmentPacket(npc.getId(),equipment );
        PacketUtils.sendPacket(clientboundSetEquipmentPacket, player);
    }

    public static GameProfile setSkin(String name, GameProfile gameProfile) {
        Gson gson = new Gson();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;

        String json = getStringFromURL(url);
        String uuid = gson.fromJson(json, JsonObject.class).get("id").getAsString();

        url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
        json = getStringFromURL(url);
        JsonObject mainObject = gson.fromJson(json, JsonObject.class);
        JsonObject jsonObject = mainObject.get("properties").getAsJsonArray().get(0).getAsJsonObject();
        String value = jsonObject.get("value").getAsString();
        String signature = jsonObject.get("signature").getAsString();
        PropertyMap propertyMap = gameProfile.getProperties();
        propertyMap.put("name", new Property("name", name));
        propertyMap.put("textures", new Property("textures", value, signature));
        return gameProfile;
    }

    public static void changeSkin(String value, String signature, GameProfile gameProfile) {
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
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
}