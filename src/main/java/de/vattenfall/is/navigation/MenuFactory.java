package de.vattenfall.is.navigation;

/**
 * Defines a generic factory for creating menu structures based on {@link MenuItem MenuItems}.
 */
public interface MenuFactory {

    /**
     * Creates a new menu.
     * <p/>
     * Concrete implementations offer methods to configure the factory. For example, if the menu is
     * configured via XML, then there <i>might</i> be a method <tt>setXml(String xml)</tt> that
     * allows to pass in the menu structure.
     * 
     * @return the {@link MenuRoot} of the navigation menu
     */
    MenuRoot build();

}