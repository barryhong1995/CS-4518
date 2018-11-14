package wpi.hphong.petshare;

public class Pet {
    private String name;
    private int age;
    private String species;
    private String image;

    public Pet() {}
    public Pet(String name, int age, String species, String image) {
        this.name = name;
        this.age = age;
        this.species = species;
        this.image = image;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public String getSpecies() { return species; }

    public void setSpecies(String species) { this.species = species; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
