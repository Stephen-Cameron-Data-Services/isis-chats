package au.com.scds.chats.dn.testing;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "DataNucleus", menuBar = MenuBar.PRIMARY, menuOrder = "100")
public class Menu {

	@MemberOrder(sequence="1")
	public ConcreteParentType createParentType() {
		ConcreteParentType baseType = null;
		try {
			baseType = container.newTransientInstance(ConcreteParentType.class);
			baseType.setName(" Parent");
			container.persistIfNotAlready(baseType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseType;
	}
	
	@MemberOrder(sequence="2")
	public ChildTypeOfConcreteParentType createChildTypeOfConcreteParentType() {
		ChildTypeOfConcreteParentType childType = null;
		try {
			childType = container.newTransientInstance(ChildTypeOfConcreteParentType.class);
			childType.setName("Child Type Of Concrete Parent Type");
			childType.setDescription("something descriptive");
			container.persistIfNotAlready(childType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childType;
	}
	
	@MemberOrder(sequence="3")
	public ChildTypeOfAbstractParentType createChildTypeOfAbstractParentType() {
		ChildTypeOfAbstractParentType childType = null;
		try {
			childType = container.newTransientInstance(ChildTypeOfAbstractParentType.class);
			childType.setName("Child Type Of Abstract Parent Type");
			childType.setDescription("something descriptive");
			container.persistIfNotAlready(childType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childType;
	}
	
	@MemberOrder(sequence="4")
	public ConcreteChildType2 createConcreteChildTypeInAbstractParentTable() {
		ConcreteChildType2 childType = null;
		try {
			childType = container.newTransientInstance(ConcreteChildType2.class);
			childType.setName("Concrete Child Type In Abstract Parent Table");
			childType.setDescription("something descriptive");
			container.persistIfNotAlready(childType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childType;
	}
	
	@MemberOrder(sequence="5")
	public ConcreteChildType3 createConcreteChildTypeInOwnTableNoParentTable() {
		ConcreteChildType3 childType = null;
		try {
			childType = container.newTransientInstance(ConcreteChildType3.class);
			childType.setName("Concrete Child Type In Own Table; No Parent Table");
			childType.setDescription("something descriptive");
			container.persistIfNotAlready(childType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childType;
	}
	
	@MemberOrder(sequence="6")
	public ConcreteParentTypeWithCollection createParentTypeWithCollection() {
		ConcreteParentTypeWithCollection baseTypeWithColln = null;
		try {
			baseTypeWithColln = container.newTransientInstance(ConcreteParentTypeWithCollection.class);
			baseTypeWithColln.setName(" Parent");
			container.persistIfNotAlready(baseTypeWithColln);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseTypeWithColln;
	}
	
	@MemberOrder(sequence="7")
	public ConcreteChildType4 createConcreteChildTypeWithCollection() {
		ConcreteChildType4 childType = null;
		try {
			childType = container.newTransientInstance(ConcreteChildType4.class);
			childType.setName("Concrete Child Type With Collection");
			childType.setDescription("something descriptive");
			container.persistIfNotAlready(childType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childType;
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}
