package components;

import Jade.Component;

public class SpriteRender extends Component {

    private boolean firstTime = false;


    @Override
    public void start() {
        System.out.println("SpriteRender Start");
    }
    @Override
    public void update(float dt) {
        if (!firstTime) {
            System.out.println("SpriteRender Update");
            firstTime = true;
        }
    }
}
