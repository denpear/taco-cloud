package sia.tacocloud;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий заказ:
 * TacoOrder, представляющий заказы, которые клиенты будут оставлять на сайте,
 * с информацией о рецепте, оплате и доставке
 */
@Data
public class TacoOrder {
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco){
        this.tacos.add(taco);
    }

}
