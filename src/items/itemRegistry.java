package items;

public class itemRegistry {
    public itemTemplate[] itemTemplates;
    public itemRegistry() {
        itemTemplates = new itemTemplate[]{
                new flag()
        };
    }

    public itemTemplate getItemTemplate(itemEnum enumKey) {
        if(itemTemplates.length < enumKey.index)
            return null;
        return itemTemplates[enumKey.index];
    }
}
