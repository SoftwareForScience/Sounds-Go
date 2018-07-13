package com.team4.soundsgo;

public class MetaData {


    private int id;
        private String userName;
        private String soundName;
        private float lattitud;
        private float longitude;
        private String category;

        public MetaData(){
            System.out.println("Creation !");
            id=0;
            userName = "NaN";
            soundName = "NaN";
            lattitud = 0;
            longitude = 0;
            this.setCategorie();
        }

        public MetaData(int i, String uName, String sName, float pNbre, float pNbre2, String cat)
        {
            id=i;
            userName = uName;
            soundName = sName;
            lattitud = pNbre;
            longitude = pNbre2;
            this.category=cat;
        }

        public String getUserName()  {
            return userName;
        }

        public String getSoundName()
        {
            return soundName;
        }

        public float getNombreHabitants()
        {
            return lattitud;
        }

        public String getCategory()
        {
            return category;
        }

        public void setUserName(String pNom)
        {
            userName = pNom;
        }

        public void setSoundName(String pPays)
        {
            soundName = pPays;
        }

        public void setLattitud(int nbre)
        {
            lattitud = nbre;
            this.setCategorie();
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private void setCategorie() {

            int bornesSuperieures[] = {0, 1000, 10000, 100000, 500000, 1000000, 5000000, 10000000};
            String categories[] = {"?", "A", "B", "C", "D","E","F","G","H"};

            int i = 0;
            while (i < bornesSuperieures.length && this.lattitud > bornesSuperieures[i])
                i++;

            this.category = categories[i];
        }
    }