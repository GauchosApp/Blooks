package com.plipapps.blooks;

import com.artemis.BaseSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.font.FreeTypeFontProvider;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.SystemProvider;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.util.EntityEngineConfiguration;
import com.kotcrab.vis.runtime.util.SpriterData;
import com.plipapps.blooks.manager.GameSceneManager;
import com.plipapps.blooks.manager.LevelSceneManager;
import com.plipapps.blooks.manager.MenuSceneManager;
import com.plipapps.blooks.system.LockSpawnerSystem;
import com.plipapps.blooks.system.SpriteBoundsCreator;
import com.plipapps.blooks.system.SpriteBoundsUpdater;
import com.plipapps.blooks.system.SquareMergeSystem;
import com.plipapps.blooks.util.Holder;

public class Blooks extends ApplicationAdapter {
	SpriteBatch batch;
	VisAssetManager manager;
	SoundController soundController;
	String scenePath;
	Scene scene;
	private GameSceneManager gameScene;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//VisAssetManager is a extension of standard LibGDX AssetManager,
		//it makes loading scene easier
		manager = new VisAssetManager(batch);
		manager.enableFreeType(new FreeTypeFontProvider());
		manager.getLogger().setLevel(Logger.ERROR);
		soundController = new SoundController(manager);
		loadMenuScene();
		//here we load our scene, this is a blocking operation and it
		//will block your app until loading entire scene is finished
	}
	public SoundController getSoundController () {
		return soundController;
	}

	public void loadMenuScene() {
		unloadPreviousScene();

		SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
		parameter.config.addSystem(SpriteBoundsCreator.class);
		parameter.config.addSystem(SpriteBoundsUpdater.class);
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create (EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				return new MenuSceneManager(Blooks.this);
			}
		});

		scenePath = "scene/menu1080.scene";
		scene = manager.loadSceneNow(scenePath, parameter);
	}
	public void loadLevelScene(){
		unloadPreviousScene();

		final Holder<LockSpawnerSystem> spawnerSystem = Holder.empty();
		SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
		parameter.config.addSystem(SpriteBoundsCreator.class);
		parameter.config.addSystem(SpriteBoundsUpdater.class);
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create (EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				return new LevelSceneManager(Blooks.this);
			}
		});
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create (EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				spawnerSystem.value = new LockSpawnerSystem();
				return spawnerSystem.value;
			}
		});

		scenePath = "scene/level1080.scene";
		scene = manager.loadSceneNow(scenePath, parameter);
	}
	public void loadGameScene (final int nivel) {
		unloadPreviousScene();

		final Holder<SquareMergeSystem> spawnerSystem = Holder.empty();

		SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
		parameter.config.addSystem(SpriteBoundsCreator.class);
		parameter.config.addSystem(SpriteBoundsUpdater.class);
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create (EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				return new GameSceneManager(Blooks.this, nivel);
			}
		});
		parameter.config.addSystem(new SystemProvider() {
			@Override
			public BaseSystem create (EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
				spawnerSystem.value = new SquareMergeSystem();
				return spawnerSystem.value;
			}
		});

		//manager.load("spriter/Animacion/Animacion.scml", SpriterData.class);
		scenePath = "scene/game1080.scene";
		scene = manager.loadSceneNow(scenePath, parameter);
	}

	private void unloadPreviousScene () {
		if (scenePath != null) {
			manager.unload(scenePath);
			scenePath = null;
			scene = null;
		}
	}
	@Override
	public void render () {
		//Gdx.gl.glClearColor(251/255f, 237/255f, 196/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.render();
	}

	@Override
	public void resize(int width, int height) {
		scene.resize(width, height);
	}

	@Override
	public void dispose () {
		//here we must release used resources, Scene itself is
		//disposable but because we loaded it via AssetsManager will dispose it for us
		batch.dispose();
		manager.dispose();
	}
}
