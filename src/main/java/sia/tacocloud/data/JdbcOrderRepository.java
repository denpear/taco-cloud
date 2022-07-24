package sia.tacocloud.data;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sia.tacocloud.IngredientRef;
import sia.tacocloud.Taco;
import sia.tacocloud.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder tacoOrder) {
        /**
         * Первый шаг – создание объекта PreparedStatementCreatorFactory,
         * описывающего запрос insert вместе с типами параметров запроса.
         * Поскольку позднее нам понадобится идентификатор сохраненного
         * заказа, мы также должны вызвать метод setReturnGeneratedKeys(true)
         * этого объекта.
         */
        PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory(
                        "insert into Taco_Order "
                                + "(delivery_name, delivery_street, delivery_city, "
                                + "delivery_state, delivery_zip, cc_number, "
                                + "cc_expiration, cc_cvv, placed_at) "
                                + "values (?,?,?,?,?,?,?,?,?)",
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
                );
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        tacoOrder.setPlacedAt(new Date());

        /**
         * После создания PreparedStatementCreatorFactory мы используем
         * его для создания объекта PreparedStatementCreator, передавая значения
         * из объекта TacoOrder, которые требуется сохранить. Последний
         * аргумент в вызове PreparedStatementCreator – это дата создания заказа,
         * которую также нужно будет установить в самом объекте TacoOrder,
         * чтобы возвращаемый экземпляр TacoOrder содержал эту информацию.
         */

        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory.newPreparedStatementCreator(Arrays.asList(
                tacoOrder.getDeliveryName(),
                tacoOrder.getDeliveryStreet(),
                tacoOrder.getDeliveryCity(),
                tacoOrder.getDeliveryState(),
                tacoOrder.getDeliveryZip(),
                tacoOrder.getCcNumber(),
                tacoOrder.getCcExpiration(),
                tacoOrder.getCcCVV(),
                tacoOrder.getPlacedAt()
        ));

        /**
         * После создания PreparedStatementCreator можно фактически со-
         * хранить заказ, вызвав метод JdbcTemplate.update(), передав ему Pre-
         * paredStatementCreator и GeneratedKeyHolder. После сохранения заказа
         * в GeneratedKeyHolder будет храниться значение поля id, назначенное
         * базой данных, которое затем следует скопировать в свойство id объ-
         * екта TacoOrder.
         */

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(preparedStatementCreator, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        tacoOrder.setId(orderId);

        List<Taco> tacos = tacoOrder.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }


        return tacoOrder;
    }

    private long saveTaco(Long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                new PreparedStatementCreatorFactory(
                        "insert into Taco"
                                + "(name, created_at, taco_order, taco_order_key) "
                                + "values (?,?,?,?)",
                        Types.VARCHAR, Types.TIMESTAMP, Types.BIGINT, Types.BIGINT
                );
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc =
                preparedStatementCreatorFactory.newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                taco.getCreatedAt(),
                                orderId,
                                orderKey));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId, taco.getIngredients());

        return tacoId;
    }

    /**
     * Метод saveIngredientRefs() циклически перебирает список объектов Ingredient
     * и сохраняет каждый в таблице Ingredient_Ref. Он также имеет локальную переменную key,
     * которая используется в качестве индекса, гарантирующего сохранность порядка
     * следования ингредиентов.
     * @param tacoId
     * @param ingredientRefs
     */
    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update("insert into Ingredient_Ref(ingredient,taco,taco_key) "
                            + "values(?,?,?)",
                    ingredientRef.getIngredient(), tacoId, key++);
        }

    }
}
