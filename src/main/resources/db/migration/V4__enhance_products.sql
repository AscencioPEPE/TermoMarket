ALTER TABLE products
    ADD COLUMN brand VARCHAR(80) NOT NULL DEFAULT 'ThermoMarket',
    ADD COLUMN category VARCHAR(80) NOT NULL DEFAULT 'Termos',
    ADD COLUMN color VARCHAR(60) NOT NULL DEFAULT 'Negro',
    ADD COLUMN material VARCHAR(80) NOT NULL DEFAULT 'Acero inoxidable',
    ADD COLUMN capacity_oz INT NOT NULL DEFAULT 20,
    ADD COLUMN image_alt VARCHAR(180) NULL;

UPDATE products
SET brand = 'Rustic Thermal',
    category = 'Termos',
    color = CASE slug
        WHEN 'termo-acero-negro-20-oz' THEN 'Negro mate'
        WHEN 'termo-blanco-sublimable-20-oz' THEN 'Blanco'
        WHEN 'termo-azul-marino-30-oz' THEN 'Azul marino'
        WHEN 'termo-rosa-pastel-20-oz' THEN 'Rosa pastel'
        WHEN 'termo-plata-clasico-30-oz' THEN 'Plata'
        WHEN 'termo-negro-premium-40-oz' THEN 'Negro'
        WHEN 'termo-blanco-perlado-30-oz' THEN 'Blanco perlado'
        WHEN 'termo-verde-militar-20-oz' THEN 'Verde militar'
        WHEN 'termo-lavanda-20-oz' THEN 'Lavanda'
        WHEN 'termo-rojo-vino-30-oz' THEN 'Rojo vino'
        WHEN 'termo-gris-oxford-30-oz' THEN 'Gris oxford'
        WHEN 'termo-cobre-metalico-20-oz' THEN 'Cobre metalico'
        ELSE color
    END,
    material = 'Acero inoxidable 304',
    capacity_oz = CASE slug
        WHEN 'termo-negro-premium-40-oz' THEN 40
        WHEN 'termo-azul-marino-30-oz' THEN 30
        WHEN 'termo-plata-clasico-30-oz' THEN 30
        WHEN 'termo-blanco-perlado-30-oz' THEN 30
        WHEN 'termo-rojo-vino-30-oz' THEN 30
        WHEN 'termo-gris-oxford-30-oz' THEN 30
        ELSE 20
    END,
    image_alt = CONCAT(name, ' color ', CASE slug
        WHEN 'termo-acero-negro-20-oz' THEN 'negro mate'
        WHEN 'termo-blanco-sublimable-20-oz' THEN 'blanco'
        WHEN 'termo-azul-marino-30-oz' THEN 'azul marino'
        WHEN 'termo-rosa-pastel-20-oz' THEN 'rosa pastel'
        WHEN 'termo-plata-clasico-30-oz' THEN 'plata'
        WHEN 'termo-negro-premium-40-oz' THEN 'negro'
        WHEN 'termo-blanco-perlado-30-oz' THEN 'blanco perlado'
        WHEN 'termo-verde-militar-20-oz' THEN 'verde militar'
        WHEN 'termo-lavanda-20-oz' THEN 'lavanda'
        WHEN 'termo-rojo-vino-30-oz' THEN 'rojo vino'
        WHEN 'termo-gris-oxford-30-oz' THEN 'gris oxford'
        WHEN 'termo-cobre-metalico-20-oz' THEN 'cobre metalico'
        ELSE 'general'
    END);
