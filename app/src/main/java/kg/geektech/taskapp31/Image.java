package kg.geektech.taskapp31;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Image {


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String image;

    public Image(String image) {
        this.image= image;
    }

    public Image() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
