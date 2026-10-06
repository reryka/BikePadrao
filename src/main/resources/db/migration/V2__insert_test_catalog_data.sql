INSERT INTO categoria (nome, slug)
VALUES
    ('Bicicletas', 'bicicletas'),
    ('Capacetes', 'capacetes'),
    ('Componentes', 'componentes');


INSERT INTO marca (nome, slug)
VALUES
    ('Caloi', 'caloi'),
    ('Giro', 'giro'),
    ('Shimano', 'shimano');


INSERT INTO produto (
    nome,
    slug,
    sku,
    descricao_curta,
    descricao,
    preco,
    preco_promocional,
    estoque,
    ativo,
    destaque,
    categoria_id,
    marca_id
)
VALUES (
           'Caloi Explorer Sport',
           'caloi-explorer-sport',
           'BIKE-CAL-001',
           'Mountain bike para trilhas e uso urbano.',
           'Bicicleta Caloi Explorer Sport indicada para quem busca utilização urbana e trilhas leves.',
           2599.90,
           2399.90,
           3,
           TRUE,
           TRUE,
           (SELECT id FROM categoria WHERE slug = 'bicicletas'),
           (SELECT id FROM marca WHERE slug = 'caloi')
       );


INSERT INTO produto (
    nome,
    slug,
    sku,
    descricao_curta,
    descricao,
    preco,
    preco_promocional,
    estoque,
    ativo,
    destaque,
    categoria_id,
    marca_id
)
VALUES (
           'Capacete Giro Fixture II',
           'capacete-giro-fixture-ii',
           'CAP-GIR-001',
           'Capacete para ciclismo com sistema de ajuste.',
           'Capacete Giro Fixture II para ciclismo recreativo, urbano e trilhas.',
           349.90,
           NULL,
           10,
           TRUE,
           TRUE,
           (SELECT id FROM categoria WHERE slug = 'capacetes'),
           (SELECT id FROM marca WHERE slug = 'giro')
       );


INSERT INTO produto (
    nome,
    slug,
    sku,
    descricao_curta,
    descricao,
    preco,
    preco_promocional,
    estoque,
    ativo,
    destaque,
    categoria_id,
    marca_id
)
VALUES (
           'Pedal Shimano PD-EF202',
           'pedal-shimano-pd-ef202',
           'PED-SHI-001',
           'Pedal plataforma Shimano em alumínio.',
           'Pedal Shimano PD-EF202 indicado para bicicletas urbanas e mountain bikes.',
           289.90,
           NULL,
           8,
           TRUE,
           FALSE,
           (SELECT id FROM categoria WHERE slug = 'componentes'),
           (SELECT id FROM marca WHERE slug = 'shimano')
       );