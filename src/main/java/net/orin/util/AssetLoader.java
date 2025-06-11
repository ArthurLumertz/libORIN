package net.orin.util;

import net.orin.audio.Music;
import net.orin.audio.Sound;
import net.orin.graphics.g2d.texture.Texture;
import net.orin.graphics.g3d.mesh.Mesh;

import java.util.*;

public class AssetLoader implements Disposable {

    private final Map<String, Object> loadedAssets = new HashMap<>();
    private final Queue<AssetDescriptor<?>> loadQueue = new LinkedList<>();
    private final Set<String> loading = new HashSet<>();
    private final OBJLoader objLoader = new OBJLoader();

    public <T> void load(String path, Class<T> type) {
        if (loadedAssets.containsKey(path) || loading.contains(path)) return;
        loadQueue.offer(new AssetDescriptor<>(path, type));
        loading.add(path);
    }

    public boolean update() {
        AssetDescriptor<?> desc = loadQueue.poll();
        if (desc == null) return true;

        Object asset = loadAssetFromFile(desc.path, desc.type);
        loadedAssets.put(desc.path, asset);
        loading.remove(desc.path);

        return loadQueue.isEmpty() && loading.isEmpty();
    }

    public boolean isLoaded(String path) {
        return loadedAssets.containsKey(path);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> type) {
        Object asset = loadedAssets.get(path);
        if (!type.isInstance(asset)) {
            throw new RuntimeException("Asset not loaded or type mismatch: " + path);
        }
        return (T) asset;
    }

    public <T> void unload(String path, Class<T> type) {
        Object asset = loadedAssets.get(path);
        if (!type.isInstance(asset)) return;

        if (asset instanceof Disposable) {
            ((Disposable) asset).dispose();
        }
    }

    public void unload(String path) {
        Object asset = loadedAssets.remove(path);
        if (asset instanceof Disposable) {
            ((Disposable) asset).dispose();
        }
    }

    public void unloadAll() {
        for (Object asset : loadedAssets.values()) {
            if (asset instanceof Disposable) {
                ((Disposable) asset).dispose();
            }
        }
        loadedAssets.clear();
        loading.clear();
        loadQueue.clear();
    }

    @Override
    public void dispose() {
        unloadAll();
    }

    public void finishLoading() {
        while (!loadQueue.isEmpty() || !loading.isEmpty()) {
            update();
        }
    }

    public float getProgress() {
        int total = loadedAssets.size() + loadQueue.size() + loading.size();
        if (total == 0) return 1f;
        return (float) loadedAssets.size() / total;
    }

    public <T> boolean hasLoaded(String path, Class<T> type) {
        return loadedAssets.containsKey(path) && type.isInstance(loadedAssets.get(path));
    }

    private <T> T loadAssetFromFile(String path, Class<T> type) {
        if (type == Texture.class) return type.cast(new Texture(path));
        if (type == Sound.class) return type.cast(new Sound(path));
        if (type == Music.class) return type.cast(new Music(path));
        if (type == Mesh.class) return type.cast(objLoader.parse(path));
        throw new IllegalArgumentException("Unsupported asset type: " + type);
    }

    private static class AssetDescriptor<T> {
        final String path;
        final Class<T> type;

        AssetDescriptor(String path, Class<T> type) {
            this.path = path;
            this.type = type;
        }
    }
}
