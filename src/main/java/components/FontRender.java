package components;

import Jade.Component;

public class FontRender extends Component {

    public FontRender() {
        System.out.println("FontRender Constructor");
    }

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRender.class) != null) {
            System.out.println(" Found font render");
        }
    }

    @Override
    public void update(float dt) {
        System.out.println("FontRender Update");
    }
}
