package gigster.com.holdsum.helper.enums;

/**
 * Created by Eshaan on 2/24/2016.
 */
public enum Mode {
    INVEST(0), BORROW(1);

    int val;

    Mode(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static Mode fromValue(int mode) {
        switch (mode) {
            case 0:
                return INVEST;
            case 1:
                return BORROW;
        }
        return null;
    }
}
