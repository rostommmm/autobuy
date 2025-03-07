package neverbuy.gui.component.itemsetting;

import neverbuy.autobuy.list.item.setting.ItemSetting;
import neverbuy.gui.component.Component;
import neverbuy.gui.component.ItemBoard;

public abstract class ItemSettingComponent extends Component {
   final ItemSetting<?> itemSetting;
   public float offset;

   public ItemSettingComponent(float[] coordinates, ItemSetting<?> itemSetting) {
      super(coordinates);
      this.itemSetting = itemSetting;
   }

   public double apply() {
      return !this.itemSetting.isVisible() ? 0.0D : this.itemSetting.getOffset();
   }

   public void updatePos(ItemBoard itemBoard) {
      this.posX = itemBoard.posX + 2.0F;
      this.posY = itemBoard.posY + 2.0F;
   }
}
