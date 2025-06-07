SELECT
    COALESCE(SUM(CASE WHEN i.valor = 5 THEN ((dv.precio_unitario * dv.cantidad) * (i.valor::numeric / 100))/(1+(i.valor::numeric / 100)) ELSE 0 END), 0) AS iva_5,
    COALESCE(SUM(CASE WHEN i.valor = 10 THEN ((dv.precio_unitario * dv.cantidad) * (i.valor::numeric / 100))/(1+(i.valor::numeric / 100)) ELSE 0 END), 0) AS iva_10
FROM detalle_ventas dv
LEFT JOIN producto p ON dv.producto_id = p.id
LEFT JOIN iva i ON p.idiva = i.id
WHERE dv.venta_id = 2;