/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pf4j.spring;

import java.util.List;
import java.util.Map;

import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

/**
 * @author Decebal Suiu
 */
public class ExtensionsInjector {

	private static final Logger log = LoggerFactory.getLogger(ExtensionsInjector.class);

	protected final SpringPluginManager springPluginManager;
	protected final AbstractAutowireCapableBeanFactory beanFactory;

	public ExtensionsInjector(SpringPluginManager springPluginManager, AbstractAutowireCapableBeanFactory beanFactory) {
		this.springPluginManager = springPluginManager;
		this.beanFactory = beanFactory;
	}

	public void injectExtensions() {
		// add extensions from classpath (non plugin)
		var extensionClasses = springPluginManager.getExtensionClasses((String) null);
		for (var extensionClass : extensionClasses) {
			log.debug("Register extension '{}' as bean", extensionClass.getName());
			registerExtension(extensionClass);
		}

		// add extensions for each started plugin
		List<PluginWrapper> startedPlugins = springPluginManager.getStartedPlugins();
		for (PluginWrapper plugin : startedPlugins) {
			log.debug("Registering extensions of the plugin '{}' as beans", plugin.getPluginId());
			extensionClasses = springPluginManager.getExtensionClasses(plugin.getPluginId());
			for (var extensionClass : extensionClasses) {
				log.debug("Register extension '{}' as bean", extensionClass.getName());
				registerExtension(extensionClass);
			}
		}
	}

	/**
	 * Register an extension as bean. Current implementation register extension as singleton using
	 * {@code beanFactory.registerSingleton()}. The extension instance is created using
	 * {@code pluginManager.getExtensionFactory().create(extensionClass)}. The bean name is the extension class name.
	 * Override this method if you wish other register strategy.
	 */
	protected void registerExtension(Class<?> extensionClass) {
		Map<String, ?> extensionBeanMap = springPluginManager.getApplicationContext().getBeansOfType(extensionClass);
		if (extensionBeanMap.isEmpty()) {
			Object extension = springPluginManager.getExtensionFactory().create(extensionClass);
			beanFactory.registerSingleton(extensionClass.getName(), extension);
		} else {
			log.debug("Bean registeration aborted! Extension '{}' already existed as bean!", extensionClass.getName());
		}
	}

}
