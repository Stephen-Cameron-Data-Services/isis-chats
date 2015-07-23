package au.com.scds.isis.viewer.wicket.viewer.registries.components;

import org.apache.isis.viewer.wicket.ui.components.entity.properties.EntityPropertiesPanelFactory;
import org.apache.isis.viewer.wicket.viewer.registries.components.ComponentFactoryRegistrarDefault;

import au.com.scds.isis.viewer.wicket.ui.components.entity.properties.MyEntityPropertiesPanelFactory;

import com.google.inject.Singleton;

@Singleton
public class MyComponentFactoryRegistrar extends ComponentFactoryRegistrarDefault {
    @Override
    public void addComponentFactories(ComponentFactoryList componentFactories) {
    	super.addComponentFactories(componentFactories);
    	componentFactories.replace(EntityPropertiesPanelFactory.class,new MyEntityPropertiesPanelFactory());
        //componentFactories.add(new MyEntityPropertiesPanelFactory());
    }
}
