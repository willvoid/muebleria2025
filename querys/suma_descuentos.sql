SELECT
    COALESCE(SUM(
        CASE 
            WHEN CURRENT_DATE BETWEEN d.desde AND d.hasta THEN 
                (dv.precio_unitario * dv.cantidad) * (d.descuento::numeric / 100)
            ELSE 0
        END
    ), 0) AS total_descuentos
FROM detalle_ventas dv
LEFT JOIN producto p ON dv.producto_id = p.id
LEFT JOIN descuentos d ON d.idproducto = p.id
WHERE dv.venta_id = 2;
