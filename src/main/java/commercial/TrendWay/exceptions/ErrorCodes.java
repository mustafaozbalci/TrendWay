package commercial.TrendWay.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodes {
    COMPANY_NOT_FOUND(1001), CATEGORY_NOT_FOUND(1002), COMPANY_ALREADY_EXISTS(1003), CATEGORY_ALREADY_EXISTS(1004), USER_NOT_FOUND(1005), PRODUCT_NOT_FOUND(1006), CART_NOT_FOUND(1007), CART_ITEM_NOT_FOUND(1008), USER_ALREADY_EXISTS(1009), ROLE_NOT_FOUND(1010), ORDER_NOT_FOUND(1011);

    private final int code;
}
